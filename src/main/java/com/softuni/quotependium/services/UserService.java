package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.UserRegisterFormDto;
import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.domain.enums.UserRoleEnum;
import com.softuni.quotependium.repositories.UserRepository;
import com.softuni.quotependium.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (!userRegisterFormDto.getPassword().equals(userRegisterFormDto.getConfirmPassword())) {
            //TODO Passwords don't match error
            return;
        }

        userRegisterFormDto.setPassword(passwordEncoder.encode(userRegisterFormDto.getPassword()));

        UserEntity mappedUser = map(userRegisterFormDto);

        setDefaultRole(mappedUser);
        this.userRepository.saveAndFlush(mappedUser);
    }

    private void setDefaultRole(UserEntity mappedUser) {
        UserRoleEntity role = this.userRoleRepository.findByRole(UserRoleEnum.USER);
        mappedUser.setRoles(List.of(role));
    }

    private UserEntity map(UserRegisterFormDto userRegisterFormDto) {
        return this.modelMapper.map(userRegisterFormDto, UserEntity.class);
    }
}
