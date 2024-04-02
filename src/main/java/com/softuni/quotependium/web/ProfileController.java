package com.softuni.quotependium.web;

import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        model.addAttribute("userProfile", this.userService.getCurrentUserProfile());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("username") String newUsername,
                                Model model,
                                BindingResult bindingResult) {

        boolean usernameExists = this.userService.usernameExists(newUsername);
        model.addAttribute("userProfile", this.userService.getCurrentUserProfile().setUsername(newUsername));

        if (usernameExists) {
            bindingResult.addError(new ObjectError("usernameError", "Username exists"));
        }

        if (newUsername.trim().isEmpty()) {
            bindingResult.addError(new ObjectError("usernameError", "Username cannot be empty"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "profile";
        }

        this.userService.updateCurrentUserUsername(newUsername);
        return "redirect:/users/logout";
    }
}