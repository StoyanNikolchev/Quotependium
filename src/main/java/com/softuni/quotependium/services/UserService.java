package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.ManageUserRolesDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Page<ManageUserRolesDto> getAllUsersToManageDto(Pageable pageable) {
        Page<UserEntity> entityPage = this.userRepository.findAll(pageable);
        return entityPage.map(this::convertToManageUserRolesDto);
    }

    private ManageUserRolesDto convertToManageUserRolesDto(UserEntity userEntity) {
        return this.modelMapper.map(userEntity, ManageUserRolesDto.class);
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

    public void updateUserRoles(Long userId, List<String> stringRoles) {
        UserEntity userEntity = this.userRepository.findById(userId).get();
        List<UserRoleEntity> roles = convertStringRolesToUserRoleEntities(stringRoles);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
    }

    private List<UserRoleEntity> convertStringRolesToUserRoleEntities(List<String> stringRoles) {
        return stringRoles.stream()
                .map(role -> {
                    UserRoleEnum enumValue = UserRoleEnum.valueOf(role);
                    return this.userRoleRepository.findByRole(enumValue);
                })
                .collect(Collectors.toList());
    }
}
