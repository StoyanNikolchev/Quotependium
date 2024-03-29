package com.softuni.quotependium.utils;
import org.springframework.security.core.context.SecurityContextHolder;
import java.security.Principal;

public class SecurityUtils {

    public static Principal getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}