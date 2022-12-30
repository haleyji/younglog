package com.younglog.config;

import com.younglog.config.data.UserSession;
import com.younglog.domain.Session;
import com.younglog.exception.Unauthorized;
import com.younglog.repository.SessionRepository;
import com.younglog.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterType().equals(UserSession.class));
        //true 일 경우, resolveArgument method 실행
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            log.info("servlet request null");
            throw new Unauthorized();
        }
        Cookie[] cookies = request.getCookies();

        if(cookies == null || cookies.length == 0){
            log.info("have no cookie");
            throw new Unauthorized();
        }

        String key = cookies[0].getName();//key
        String accessToken = cookies[0].getValue();//value


        Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);


        return new UserSession(session.getUser().getId());
    }
}
