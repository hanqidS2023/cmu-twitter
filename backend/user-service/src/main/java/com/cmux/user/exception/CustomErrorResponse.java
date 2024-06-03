package com.cmux.user.exception;

public class CustomErrorResponse {

    private String errorMessage;
    private int statusCode;
    private long timestamp = System.currentTimeMillis();

    public CustomErrorResponse(String errorMessage, int statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public CustomErrorResponse() {
    }  

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }  

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
