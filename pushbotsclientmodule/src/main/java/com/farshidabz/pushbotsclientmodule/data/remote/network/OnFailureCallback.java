package com.farshidabz.pushbotsclientmodule.data.remote.network;

public interface OnFailureCallback<E> {
    void onFailure(E throwable);
}
