package com.example.gunialarm.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/alarm")
    public String alarm(HttpServletRequest request, ModelMap model){
        System.out.println("alarm 페이지 접근");
        return "user/alarm";
    }

    @GetMapping("/alarm2")
    public String alarm2(HttpServletRequest request, ModelMap model){
        System.out.println("alarm 페이지 접근");
        return "forward:/META-INF/resources/WEB-INF/jsp/user/alarm.jsp";
    }

    @GetMapping("/alarm3")
    public String alarm3(HttpServletRequest request, ModelMap model){
        System.out.println("alarm 페이지 접근3");
        return "user/alarm.jsp";
    }
    @GetMapping("/alarm4")
    public String alarm4(HttpServletRequest request, ModelMap model){
        System.out.println("alarm 페이지 접근4");
        return "/user/alarm";
    }
}
