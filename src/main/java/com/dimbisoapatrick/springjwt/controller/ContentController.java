package com.dimbisoapatrick.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/req")
public class ContentController {

    @GetMapping("/signup")
    public String signup() {
        return "redirect:/req/register";
    }

    @GetMapping("/index")
    public String home() {
        return "redirect:/req/expenses";
    }

}
