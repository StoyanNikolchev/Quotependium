package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.UserRegisterFormDto;
import com.softuni.quotependium.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public String getRegister(@ModelAttribute UserRegisterFormDto userRegisterFormDto, Model model) {
        model.addAttribute("userRegisterFormDto", userRegisterFormDto);
        return "register";
    }

    @PostMapping("/users/register")
    public String postRegister(@Valid UserRegisterFormDto userRegisterFormDto, BindingResult bindingResult) {
        if (this.userService.usernameExists(userRegisterFormDto.getUsername())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "username", "Username already in use"));
        }

        if (this.userService.emailExists(userRegisterFormDto.getEmail())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "email", "Email already in use"));
        }

        if (!userRegisterFormDto.getPassword().equals(userRegisterFormDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "confirmPassword", "Passwords must match"));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        this.userService.registerUser(userRegisterFormDto);
        return "redirect:/";
    }
}


