package com.younglog.config;

import com.younglog.config.data.UserSession;
import com.younglog.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterType().equals(UserSession.class));
        //true 일 경우, resolveArgument method 실행
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String auth = webRequest.getHeader("Authorization");
        if(auth == null || auth.equals("")){
            throw new Unauthorized();
        }


        //DB 사용자 확인 작업
        //...


        return new UserSession(1L);
    }
}
