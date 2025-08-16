package com.dimbisoapatrick.springjwt.controller;

import com.dimbisoapatrick.springjwt.service.UserService;
import com.dimbisoapatrick.springjwt.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/req")
public class AuthController {

    private final UserService userService;

    @GetMapping({"/login", "/"})
    public String showLoginPage() {
        return "req/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "req/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO,
                           BindingResult result,
                           Model model) {
        System.out.println("Printing the user details:"+userDTO);
        if (result.hasErrors()) {
            return "req/register";
        }
        userService.save(userDTO);
        model.addAttribute("successMsg", true);
        return "req/login";
    }
}













