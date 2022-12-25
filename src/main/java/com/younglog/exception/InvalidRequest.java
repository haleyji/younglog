package com.younglog.exception;

//status -> 400
public class InvalidRequest extends YounglogException{
    private static final String MESSAGE = "잘못된 요청입니다";
    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
