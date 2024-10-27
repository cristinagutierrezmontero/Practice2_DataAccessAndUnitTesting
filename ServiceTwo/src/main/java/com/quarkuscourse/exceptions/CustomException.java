package com.quarkuscourse.exceptions;

public class CustomException extends Throwable {

    private final int errorCode;
    private final String errorMsg;

    public CustomException( int errorCode, String errorMsg) {

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return errorCode + ":" + errorMsg;
    }

}