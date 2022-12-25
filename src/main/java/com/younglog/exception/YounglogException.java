package com.younglog.exception;

public abstract class YounglogException extends RuntimeException {

    public YounglogException(String message) {
        super(message);
    }

    public YounglogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();
}
