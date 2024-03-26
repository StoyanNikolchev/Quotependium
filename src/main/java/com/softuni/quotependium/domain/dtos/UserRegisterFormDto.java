package com.softuni.quotependium.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserRegisterFormDto {

    @NotBlank(message = "Enter your username")
    private String username;

    @NotBlank(message = "Enter your password")
    @Length(min = 6, message = "Passwords must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Confirm your password")
    private String confirmPassword;

    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Enter your full name")
    private String fullName;

    public String getUsername() {
        return username;
    }

    public UserRegisterFormDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterFormDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterFormDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterFormDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRegisterFormDto setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}
