package com.younglog.config;

import com.younglog.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("{}", "prehandle >>>>>");
        String accessToken = request.getParameter("accessToken");
        if (accessToken!=null && !accessToken.equals("")) {
            request.setAttribute("userName", accessToken);
            return true;
        }
        throw new Unauthorized();
        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("{}", "posthandle >>>>>");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("{}", "after completion >>>>>");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
