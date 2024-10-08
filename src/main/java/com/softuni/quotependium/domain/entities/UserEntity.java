package com.softuni.quotependium.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(name = "full_name", nullable = false)
    @Size(min = 5, max = 20)
    private String fullName;

    @Column(unique = true)
    private String profilePicturePath;

    @ManyToMany
    @JoinTable(
            name = "user_liked_quotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "quote_id")
    )
    private Set<QuoteEntity> likedQuotes = new HashSet<>();

    public Set<QuoteEntity> getLikedQuotes() {
        return likedQuotes;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public UserEntity setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        return this;
    }

    public UserEntity setLikedQuotes(Set<QuoteEntity> likedQuotes) {
        this.likedQuotes = likedQuotes;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRoleEntity> roles;

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public UserEntity setId(Long id) {
        super.setId(id);
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
