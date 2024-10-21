package com.example.gunialarm.classes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

 

public class Interceptor implements HandlerInterceptor {

    private final String [] passUrls = {
        
    };

    private final String [] verificationUrls = {
       
    };
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 전에 실행할 작업 구현
    	HttpSession session = request.getSession();
        String url = request.getRequestURI();
    	String[] urlArr = request.getRequestURI().split("/");
       
        return true; // true를 반환하면 요청을 계속 진행, false를 반환하면 요청을 중단
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 요청 후에 실행할 작업 구현
   
    }
}