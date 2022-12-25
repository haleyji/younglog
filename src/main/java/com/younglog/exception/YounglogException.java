package com.younglog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public abstract class YounglogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();
    public YounglogException(String message) {
        super(message);
    }

    public YounglogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message) {
    validation.put(fieldName, message);
    }

}
