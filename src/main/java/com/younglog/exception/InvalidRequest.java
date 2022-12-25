package com.younglog.exception;

import lombok.Getter;

//status -> 400
@Getter
public class InvalidRequest extends YounglogException{
    private static final String MESSAGE = "잘못된 요청입니다";

//    private String fieldName;
//    private String message;
//    따로 받는 모습이 nice 해 보이지 않음

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
//        this.fieldName = fieldName;
//        this.message = message;
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
