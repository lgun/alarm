package com.example.gunialarm.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/alarm")
    public String main(HttpServletRequest request, ModelMap model){
        System.out.println("alarm 페이지 접근");
        return "user/alarm";
    }
}
