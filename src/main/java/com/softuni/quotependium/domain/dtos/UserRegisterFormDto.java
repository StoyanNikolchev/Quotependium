package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.enums.UserRoleEnum;

import java.util.List;

public class UserRegisterFormDto {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
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
