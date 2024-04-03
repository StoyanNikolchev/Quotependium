package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.entities.UserRoleEntity;

import java.util.List;

public class ManageUserRolesDto {
    private Long id;
    private String username;
    private List<UserRoleEntity> roles;

    public Long getId() {
        return id;
    }

    public ManageUserRolesDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ManageUserRolesDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public ManageUserRolesDto setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}
