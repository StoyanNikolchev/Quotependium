package com.softuni.quotependium.web;

import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

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
                                @ModelAttribute("profilePicture") MultipartFile profilePicture,
                                Model model,
                                BindingResult bindingResult) throws MaxUploadSizeExceededException{

        boolean usernameExists = this.userService.usernameExists(newUsername);
        boolean usernameIsTheSame = this.userService.userNameIsTheSame(newUsername);
        model.addAttribute("userProfile", this.userService.getCurrentUserProfile().setUsername(newUsername));

        if (usernameExists && !usernameIsTheSame) {
            bindingResult.addError(new ObjectError("usernameError", "Username exists"));
        }

        if (newUsername.trim().isEmpty()) {
            bindingResult.addError(new ObjectError("usernameError", "Username cannot be empty"));
        }

        if (!profilePicture.isEmpty()) {
            try {
                userService.updateCurrentUserProfilePicture(profilePicture);

            } catch (IOException ex) {
                bindingResult.addError(new ObjectError("profilePictureError", "Error uploading profile picture"));
                model.addAttribute("userProfile", userService.getCurrentUserProfile());
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "profile";
        }

        if (!usernameIsTheSame) {
            this.userService.updateCurrentUserUsername(newUsername);
            return "redirect:/users/logout";
        }

        return "redirect:/profile";
    }
}