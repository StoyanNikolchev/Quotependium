package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.UserRegisterFormDto;
import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/users/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/users/register")
    public String postRegister(@ModelAttribute UserRegisterFormDto userRegisterFormDto) {
        this.userService.registerUser(userRegisterFormDto);
        return "redirect:/";
    }
}


