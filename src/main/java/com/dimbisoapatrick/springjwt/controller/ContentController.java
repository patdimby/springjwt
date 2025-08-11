package com.dimbisoapatrick.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/req")
public class ContentController {

    @GetMapping("/login")
    public String login() {
        return "req/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "req/register";
    }

    @GetMapping("/index")
    public String home() {
        return "req/expenses-list";
    }

}
