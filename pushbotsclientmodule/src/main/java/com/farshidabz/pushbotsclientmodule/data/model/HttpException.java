package com.farshidabz.pushbotsclientmodule.data.model;

/**
 * Created by Farshid since 18 Oct 2019
 * <p>
 * {@link HttpException} to return api call errors in a object contains errorCode and message
 */
public class HttpException {
    int errorCode;
    String message;

    public HttpException() {
    }

    public HttpException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
