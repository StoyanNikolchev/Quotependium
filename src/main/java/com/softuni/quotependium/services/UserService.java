package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.UserRegisterFormDto;
import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.domain.enums.UserRoleEnum;
import com.softuni.quotependium.domain.views.UserProfileView;
import com.softuni.quotependium.repositories.UserRepository;
import com.softuni.quotependium.repositories.UserRoleRepository;
import com.softuni.quotependium.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    public void registerUser(UserRegisterFormDto userRegisterFormDto) {
        userRegisterFormDto.setPassword(passwordEncoder.encode(userRegisterFormDto.getPassword()));

        UserEntity mappedUser = map(userRegisterFormDto);

        setDefaultRole(mappedUser);
        this.userRepository.saveAndFlush(mappedUser);
    }

    public Long getCurrentUserId() {
        String currentUserUsername = SecurityUtils.getCurrentUser().getName();
        return this.userRepository.findUserEntityByUsername(currentUserUsername).get().getId();
    }

    public boolean emailExists(String email) {
        return this.userRepository.findUserEntityByEmail(email).isPresent();
    }

    public UserProfileView getCurrentUserProfile() {
        return UserProfileView.getFromEntity(getCurrentUserEntity());
    }

    private UserEntity getCurrentUserEntity() {
        return this.userRepository.findById(getCurrentUserId()).get();
    }

    public boolean usernameExists(String username) {
        Optional<UserEntity> userEntityByUsername = this.userRepository.findUserEntityByUsername(username);
        return userEntityByUsername.map(user -> user.getUsername().equalsIgnoreCase(username)).orElse(false);
    }

    private void setDefaultRole(UserEntity mappedUser) {
        UserRoleEntity role = this.userRoleRepository.findByRole(UserRoleEnum.USER);
        mappedUser.setRoles(List.of(role));
    }

    private UserEntity map(UserRegisterFormDto userRegisterFormDto) {
        return this.modelMapper.map(userRegisterFormDto, UserEntity.class);
    }

    public void updateCurrentUserUsername(String newUsername) {
        UserEntity currentUser = getCurrentUserEntity();
        currentUser.setUsername(newUsername);
        userRepository.saveAndFlush(currentUser);
    }
}
