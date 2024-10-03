package com.eden.api.schemas;

public class ErrorResponse {

    private String httpStatus;
    private String message;

    public ErrorResponse(String httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
