package com.farshidabz.pushbotsclientmodule.data.remote.network;

public interface OnResponseCallback<T> {
    void onResponse(T response);
}
