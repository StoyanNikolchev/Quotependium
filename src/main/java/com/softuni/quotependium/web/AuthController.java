package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.UserRegisterFormDto;
import com.softuni.quotependium.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.softuni.quotependium.domain.enums.Constants.*;

@Controller
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
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
    public String postRegister(@Valid UserRegisterFormDto userRegisterFormDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (this.userService.usernameExists(userRegisterFormDto.getUsername())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "username", USERNAME_TAKEN));
        }

        if (this.userService.emailExists(userRegisterFormDto.getEmail())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "email", EMAIL_TAKEN));
        }

        if (!userRegisterFormDto.getPassword().equals(userRegisterFormDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegisterFormDto", "confirmPassword", PASSWORDS_MISMATCH));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        this.userService.registerUser(userRegisterFormDto);
        this.logger.info("User with username: '{}' successfully registered.", userRegisterFormDto.getUsername());

        redirectAttributes.addFlashAttribute("successMessage", "Successfully registered. Please login.");
        return "redirect:/users/login";
    }

    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/users/login";
    }
}