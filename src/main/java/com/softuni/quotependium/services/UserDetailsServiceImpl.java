package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.
                findUserEntityByUsername(username)
                .map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private UserDetails map(UserEntity userEntity) {
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                getAuthorities(userEntity)
        );
    }

    private List<GrantedAuthority> getAuthorities(UserEntity userEntity) {
        return userEntity.
                getRoles().
                stream().
                map(this::mapRole)
                .toList();
    }

    private GrantedAuthority mapRole(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLES_" + userRoleEntity.getRole().name());
    }
}