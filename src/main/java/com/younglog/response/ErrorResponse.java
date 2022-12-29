package com.younglog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;

import java.util.HashMap;
import java.util.Map;


@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)//비어있지 않은 value 만 내려가겠다
public class ErrorResponse {

    private final String code;
    private final String message;

    private Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message, Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }
    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);

    }
}
