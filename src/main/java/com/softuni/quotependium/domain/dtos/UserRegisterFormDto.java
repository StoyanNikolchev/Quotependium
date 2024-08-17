package com.softuni.quotependium.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static com.softuni.quotependium.domain.enums.Messages.*;

public class UserRegisterFormDto {

    @NotBlank(message = BLANK_USERNAME)
    @Length(min = 3, max = 20, message = INVALID_USERNAME_LENGTH)
    private String username;

    @NotBlank(message = BLANK_PASSWORD)
    @Length(min = 6, max = 20, message = INVALID_PASSWORD_LENGTH)
    private String password;

    @NotBlank(message = BLANK_CONFIRM_PASSWORD)
    private String confirmPassword;

    @NotBlank(message = BLANK_EMAIL)
    @Email(message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = BLANK_FULL_NAME)
    @Length(min = 5, max = 20, message = INVALID_FULL_NAME_LENGTH)
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
