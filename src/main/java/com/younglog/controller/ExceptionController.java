package com.younglog.controller;

import com.younglog.exception.InvalidRequest;
import com.younglog.exception.PostNotFound;
import com.younglog.exception.YounglogException;
import com.younglog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }
    @ResponseBody
    @ExceptionHandler(YounglogException.class)
    public ResponseEntity<ErrorResponse> younglogException(YounglogException e) {

        int statusCode = e.statusCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

//        if (e instanceof InvalidRequest) {
//            InvalidRequest invalidRequest = (InvalidRequest) e;
//            String fieldName = invalidRequest.getFieldName();
//            String message = invalidRequest.getMessage();
//            response.addValidation(fieldName, message);
//        }
        //validation -> title : 제목에 바보를 포함할 수 없습니다
        return ResponseEntity.status(statusCode)
                .body(response);

    }

    //service 가 커질때마다 exception case 도 늘어남

}
