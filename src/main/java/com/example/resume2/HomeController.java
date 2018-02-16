package com.example.resume2;

import org.springframework.web.bind.annotation.RequestMapping;

public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }
}