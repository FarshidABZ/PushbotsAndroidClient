package com.farshidabz.pushbotsclientmodule.data.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Farshid since 18 Oct 2019
 * <p>
 * {@link BaseModel} a base class of our models that extended of {@link JSONObject} to
 * serialize and deserialize objects into {@link JSONObject}
 */
public class BaseModel extends JSONObject {
    int statusCode;
    String message;

    public BaseModel(String jsonString) throws JSONException {
        super(jsonString);
    }

    public BaseModel() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
