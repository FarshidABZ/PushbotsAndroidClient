package com.farshidabz.pushbotsclientmodule.utils;

import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Copy from {@link "https://github.com/pushbots/pushbots-react-native/blob/master/android/src/main/java/com/pushbots/reactNative/RNPushbotsUtils.java"}
 */
public class ReactNativeJsonConverter {

    private static final Map<Class<?>, Setter> SETTERS = new HashMap<>();

    static {
        SETTERS.put(Boolean.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                bundle.putBoolean(key, (Boolean) value);
            }
        });
        SETTERS.put(Integer.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                bundle.putInt(key, (Integer) value);
            }
        });
        SETTERS.put(Long.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                bundle.putLong(key, (Long) value);
            }
        });
        SETTERS.put(Double.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                bundle.putDouble(key, (Double) value);
            }
        });
        SETTERS.put(String.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                bundle.putString(key, (String) value);
            }
        });
        SETTERS.put(String[].class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) {
                throw new IllegalArgumentException("Unexpected type from JSON");
            }
        });

        SETTERS.put(JSONArray.class, new Setter() {
            public void setOnBundle(Bundle bundle, String key, Object value) throws JSONException {
                JSONArray jsonArray = (JSONArray) value;
                ArrayList<String> stringArrayList = new ArrayList<>();
                // Empty list, can't even figure out the type, assume an ArrayList<String>
                if (jsonArray.length() == 0) {
                    bundle.putStringArrayList(key, stringArrayList);
                    return;
                }

                // Only strings are supported for now
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object current = jsonArray.get(i);
                    if (current instanceof String) {
                        stringArrayList.add((String) current);
                    } else {
                        throw new IllegalArgumentException("Unexpected type in an array: " + current.getClass());
                    }
                }
                bundle.putStringArrayList(key, stringArrayList);
            }
        });
    }

    public interface Setter {
        void setOnBundle(Bundle bundle, String key, Object value) throws JSONException;
    }

    public static WritableMap toWritableMap(JSONObject jsonObject) {
        WritableNativeMap result = new WritableNativeMap();

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);

                if (JSONObject.NULL.equals(value)) {
                    result.putNull(key);
                } else if (value instanceof JSONObject) {
                    result.putMap(key, toWritableMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    result.putArray(key, toWritableArray((JSONArray) value));
                } else if (value instanceof Boolean) {
                    result.putBoolean(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    result.putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    result.putString(key, String.valueOf(value));
                } else if (value instanceof String) {
                    result.putString(key, (String) value);
                } else if (value instanceof Double) {
                    result.putDouble(key, (Double) value);
                } else {
                    Log.e("PB3", "Issue converting notification object " + value.toString());
                }

            } catch (JSONException e) {
                Log.e("PB3", "Could not convert object with key " + key + e);
            }
        }

        return result;
    }

    private static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return toJSONArray(o);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (Objects.requireNonNull(o.getClass().getPackage()).getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static JSONArray toJSONArray(Object array) throws JSONException {
        JSONArray result = new JSONArray();
        if (!array.getClass().isArray()) {
            throw new JSONException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        for (int i = 0; i < length; ++i) {
            result.put(wrap(Array.get(array, i)));
        }
        return result;
    }

    private static WritableArray toWritableArray(JSONArray array) {
        WritableNativeArray result = new WritableNativeArray();

        for (int i = 0; i < array.length(); i++) {
            try {
                Object value = array.get(i);
                if (value == null) {
                    result.pushNull();
                } else if (value instanceof JSONObject) {
                    result.pushMap(toWritableMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    result.pushArray(toWritableArray((JSONArray) value));
                } else if (value instanceof Boolean) {
                    result.pushBoolean((Boolean) value);
                } else if (value instanceof Integer) {
                    result.pushInt((Integer) value);
                } else if (value instanceof Long) {
                    result.pushString(String.valueOf(value));
                } else if (value instanceof String) {
                    result.pushString((String) value);
                } else if (value instanceof Double) {
                    result.pushDouble((Double) value);
                } else {
                    Log.e("PB3", "Issue converting notification object " + value.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("PB3", "Could not convert object with index i " + i + " in array " + array.toString() + e);
            }
        }

        return result;
    }

    public static Bundle convertToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> jsonIterator = jsonObject.keys();
        while (jsonIterator.hasNext()) {
            String key = jsonIterator.next();
            Object value = jsonObject.get(key);
            if (value == JSONObject.NULL) {
                // Null is not supported.
                continue;
            }

            // Special case JSONObject as it's one way, on the return it would be Bundle.
            if (value instanceof JSONObject) {
                bundle.putBundle(key, convertToBundle((JSONObject) value));
                continue;
            }

            Setter setter = SETTERS.get(value.getClass());
            if (setter == null) {
                throw new IllegalArgumentException("Unsupported type: " + value.getClass());
            }
            setter.setOnBundle(bundle, key, value);
        }

        return bundle;
    }
}
