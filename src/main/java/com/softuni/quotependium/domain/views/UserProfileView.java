package com.softuni.quotependium.domain.views;

import com.softuni.quotependium.domain.entities.UserEntity;

public class UserProfileView {
    private String username;
    private String email;
    private String fullName;
    private String profilePictureName;

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public UserProfileView setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserProfileView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public static UserProfileView getFromEntity(UserEntity userEntity) {
        return new UserProfileView()
                .setEmail(userEntity.getEmail())
                .setUsername(userEntity.getUsername())
                .setFullName(userEntity.getFullName())
                .setProfilePictureName(userEntity.getProfilePictureName());
    }
}
