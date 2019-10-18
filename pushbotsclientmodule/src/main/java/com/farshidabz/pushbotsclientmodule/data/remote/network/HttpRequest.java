package com.farshidabz.pushbotsclientmodule.data.remote.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.farshidabz.pushbotsclientmodule.data.model.BaseModel;
import com.farshidabz.pushbotsclientmodule.data.model.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Farshid since 18 Oct 2019
 * <p>
 * {@link HttpRequest} a class to handle http requests in background thread.
 */

public class HttpRequest extends AsyncTask<Object, Integer, BaseModel> {
    private static final String TAG = HttpRequest.class.getSimpleName();

    public enum RequestMethod {
        GET("GET"), POST("POST");

        private final String requestMethod;

        RequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getValue() {
            return requestMethod;
        }
    }

    private String url;
    private OnResponseCallback onResponseCallback;
    private OnFailureCallback onFailureCallback;
    private RequestMethod method;
    private int statusCode;
    private String message;
    private JSONObject body = null;
    private String token;

    private HttpRequest() {

    }

    /**
     * doInBackground
     * <p>
     * call an API and wait for response
     */
    @Override
    protected BaseModel doInBackground(Object... voids) {
        try {
            HttpURLConnection connection = getHttpConnection();
            connection.connect();

            int statusCode = connection.getResponseCode();
            this.statusCode = statusCode;
            this.message = connection.getResponseMessage();

            InputStreamReader streamReader;

            if (statusCode / 100 == 2) {
                streamReader = new InputStreamReader(connection.getInputStream());
            } else {
                streamReader = new InputStreamReader(connection.getErrorStream());
            }

            BaseModel responseBaseModel = new BaseModel(convertInputStreamToString(streamReader));
            responseBaseModel.setStatusCode(statusCode);

            return responseBaseModel;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * getHttpConnection
     * <p>
     * create a http connection object, set headers, method and body
     *
     * @return HttpURLConnection
     */
    private HttpURLConnection getHttpConnection() throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.getValue());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);

        if (!token.isEmpty()) {
            connection.setRequestProperty("x-pushbots-appid", token);
        }

        if (method == RequestMethod.POST) {
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (body != null) {
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(body.toString());
                    writer.flush();
                } catch (Exception e) {
                    Log.e(TAG, "set http request body " + e.getMessage());
                }
            }
        }

        return connection;
    }

    /**
     * onPostExecute
     * <p>
     * check if api call response successfully and call suitable callback.
     */
    @Override
    protected void onPostExecute(BaseModel baseModel) {
        if (baseModel == null || baseModel.getStatusCode() / 100 != 2) {
            HttpException httpException = new HttpException(statusCode, baseModel == null ? "unknown error" : baseModel.getMessage());
            onFailureCallback.onFailure(httpException);
        } else {
            onResponseCallback.onResponse(baseModel);
        }
    }

    /**
     * convertInputStreamToString
     *
     * @param inputStreamReader is {@link InputStreamReader} is api response input stream
     * @return String, converted api response to string.
     */
    private static String convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        if (inputStreamReader == null) {
            return "";
        }

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();

        String inputLine;
        String result;

        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }

        reader.close();
        inputStreamReader.close();
        return stringBuilder.toString();
    }

    /**
     * {@link HttpRequest} Builder class.
     * to initialize {@link HttpRequest} variables like Url, method, body and etc.
     */
    static public class Builder {
        HttpRequest httpRequest = new HttpRequest();

        public Builder setContext(Context context) {
            return this;
        }

        public Builder setUrl(String url) {
            httpRequest.url = url;
            return this;
        }

        public Builder setRequestMethod(RequestMethod method) {
            httpRequest.method = method;
            return this;
        }

        public Builder setBody(JSONObject body) {
            httpRequest.body = body;
            return this;
        }

        public Builder setToken(String token) {
            httpRequest.token = token;
            return this;
        }

        public HttpRequest get() {
            return httpRequest;
        }

        public HttpRequest run(OnResponseCallback onResponseCallback,
                               OnFailureCallback onFailureCallback) {
            httpRequest.onResponseCallback = onResponseCallback;
            httpRequest.onFailureCallback = onFailureCallback;
            httpRequest.execute();
            return httpRequest;
        }

        public Builder() {
        }
    }
}
