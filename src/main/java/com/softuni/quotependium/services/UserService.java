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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private Resource profilePicturesResource;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper) throws IOException {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.profilePicturesResource = initializeProfilePicturesResource();
    }

    private Resource initializeProfilePicturesResource() throws IOException {
        String path = "target/classes/static/profilePictures";
        Path directoryPath = Path.of(path);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        return new ClassPathResource("static/profilePictures");
    }

    public void registerUser(UserRegisterFormDto userRegisterFormDto) {
        userRegisterFormDto.setPassword(passwordEncoder.encode(userRegisterFormDto.getPassword()));

        UserEntity mappedUser = map(userRegisterFormDto);

        setDefaultRole(mappedUser);
        this.userRepository.saveAndFlush(mappedUser);
    }

    public Long getCurrentUserId() {
        Principal currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        String currentUserUsername = currentUser.getName();
        Optional<UserEntity> currentUserOptional = this.userRepository.findUserEntityByUsername(currentUserUsername);
        return currentUserOptional.isPresent()
                ? currentUserOptional.get().getId()
                : null;
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

    @Transactional
    public void updateCurrentUserProfilePicture(MultipartFile profilePicture) throws IOException {
        UserEntity currentUserEntity = getCurrentUserEntity();
        String userId = currentUserEntity.getId().toString();

        File profilePicturesDir = profilePicturesResource.getFile();

        try (Stream<Path> stream = Files.walk(profilePicturesDir.toPath())) {
            stream.filter(path -> path.getFileName().toString().startsWith(userId + "_"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = userId + "_" + profilePicture.getOriginalFilename();
        Path filePath = profilePicturesDir.toPath().resolve(fileName);
        Files.copy(profilePicture.getInputStream(), filePath);

        String relativeFilePath = Paths.get("profilePictures", fileName).toString();
        currentUserEntity.setProfilePicturePath(relativeFilePath);
        userRepository.save(currentUserEntity);
    }

    private List<UserRoleEntity> convertStringRolesToUserRoleEntities(List<String> stringRoles) {
        return stringRoles.stream()
                .map(role -> {
                    UserRoleEnum enumValue = UserRoleEnum.valueOf(role);
                    return this.userRoleRepository.findByRole(enumValue);
                })
                .collect(Collectors.toList());
    }

    public boolean userNameIsTheSame(String newUsername) {
        return getCurrentUserEntity().getUsername().equals(newUsername.trim());
    }
}
