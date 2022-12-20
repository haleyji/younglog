package com.younglog.controller;

import com.younglog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PostController {
    @PostMapping(value = "/posts", produces = "application/json;charset=utf-8;")
    public Map<String,String> post(@RequestBody @Valid PostCreate postCreate, BindingResult result) throws Exception{
//        데이터를 검증하는 이유
//        client 개발자가 실수로 값을 안보낼수 있음, bug로 값이 누락될 수 있다,
//        외부에서 값을 조작해서 보낼 수 있다, db에 값을 저장할 때 의도치 않은 에러가 발생할 수 있다
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();
            String errorMsg = firstFieldError.getDefaultMessage();
            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMsg);
            return error;

        }
        log.info("{}", postCreate.toString());
        return Map.of();
    }
}
