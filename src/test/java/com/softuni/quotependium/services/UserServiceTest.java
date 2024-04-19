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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        UserRegisterFormDto userDto = new UserRegisterFormDto();
        userDto.setUsername("Test User");
        userDto.setPassword("password");

        UserRoleEntity role = new UserRoleEntity().setRole(UserRoleEnum.USER);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encoded password");
        when(userRoleRepository.findByRole(UserRoleEnum.USER)).thenReturn(role);
        when(modelMapper.map(userDto, UserEntity.class))
                .thenReturn(new UserEntity()
                        .setId(1L)
                        .setUsername(userDto.getUsername())
                        .setPassword("encoded password")
                        .setFullName(userDto.getFullName())
                        .setEmail(userDto.getEmail()));

        // Act
        userService.registerUser(userDto);

        // Assert
        verify(passwordEncoder, times(1)).encode("password");
        assertEquals("encoded password", userDto.getPassword());

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).saveAndFlush(userEntityCaptor.capture());
        UserEntity capturedEntity = userEntityCaptor.getValue();

        assertEquals(1L, capturedEntity.getId());
        assertEquals(userDto.getUsername(), capturedEntity.getUsername());
        assertEquals("encoded password", capturedEntity.getPassword());
        assertEquals(userDto.getFullName(), capturedEntity.getFullName());
        assertEquals(userDto.getEmail(), capturedEntity.getEmail());
        assertEquals(List.of(role), capturedEntity.getRoles());
    }

    @Test
    public void whenGivenValidEmail_emailExists_shouldReturnTrue() {
        //ARRANGE
        String emailAddress = "test@email.com";
        when(userRepository.findUserEntityByEmail(emailAddress)).thenReturn(Optional.of(new UserEntity()));

        //ACT
        boolean result = userService.emailExists(emailAddress);

        //ASSERT
        assertTrue(result);
    }

    @Test
    public void whenGivenInvalidEmail_emailExists_shouldReturnFalse() {
        //ARRANGE
        String emailAddress = "test@email.com";
        when(userRepository.findUserEntityByEmail(emailAddress)).thenReturn(Optional.empty());

        //ACT
        boolean result = userService.emailExists(emailAddress);

        //ASSERT
        assertFalse(result);
    }

    @Test
    public void testGetCurrentUserProfile() {
        //ARRANGE
        String username = "Test User";
        Long userId = 1L;
        UserEntity userEntity = new UserEntity().setId(userId).setUsername(username);

        try (MockedStatic<SecurityUtils> securityUtilsMock = Mockito.mockStatic(SecurityUtils.class)) {
            Principal principal = mock(Principal.class);
            securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(principal);
            when(principal.getName()).thenReturn(username);

            when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

            //ACT
            UserProfileView resultProfileView = userService.getCurrentUserProfile();

            //ASSERT
            assertEquals(username, resultProfileView.getUsername());
        }
    }

    @Test
    public void whenGivenValidUsername_usernameExists_shouldReturnTrue() {
        //ARRANGE
        String username = "Test Username";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(new UserEntity().setUsername(username)));

        //ACT
        boolean result = userService.usernameExists(username);

        //ASSERT
        assertTrue(result);
    }

    @Test
    public void whenGivenInvalidUsername_usernameExists_shouldReturnFalse() {
        //ARRANGE
        String username = "Test Username";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        //ACT
        boolean result = userService.usernameExists(username);

        //ASSERT
        assertFalse(result);
    }

    @Test
    public void getAllUsersToManageDto_shouldReturnPageOfManageUserRolesDto() {
        //ARRANGE
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity().setUsername("Pesho Baftata"));
        userList.add(new UserEntity().setUsername("Besho Paftata"));

        Pageable pageable = mock(Pageable.class);
        Page<UserEntity> page = new PageImpl<>(userList);

        when(userRepository.findAll(pageable)).thenReturn(page);

        when(modelMapper.map(any(UserEntity.class), eq(ManageUserRolesDto.class)))
                .thenAnswer(invocation -> {
                    UserEntity userEntity = invocation.getArgument(0);
                    return new ManageUserRolesDto().setUsername(userEntity.getUsername());
                });

        //ACT
        Page<ManageUserRolesDto> result = userService.getAllUsersToManageDto(pageable);

        //ASSERT
        assertNotNull(result);
        assertEquals("Pesho Baftata", result.getContent().get(0).getUsername());
        assertEquals("Besho Paftata", result.getContent().get(1).getUsername());
        assertEquals(2, result.getContent().size());

        verify(modelMapper, times(2)).map(any(UserEntity.class), eq(ManageUserRolesDto.class));
    }

    @Test
    public void updateCurrentUserUsername_givenNewUsername_shouldUpdateUsername() {
        //ARRANGE
        String username = "Test Username";
        Long id = 1L;
        UserEntity currentUser = new UserEntity().setUsername(username).setId(id);
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(currentUser);

        try (MockedStatic<SecurityUtils> securityUtilsMock = Mockito.mockStatic(SecurityUtils.class)) {
            Principal principal = mock(Principal.class);
            securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(principal);
            when(principal.getName()).thenReturn(username);

            when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(currentUser));
            when(userRepository.findById(id)).thenReturn(Optional.of(currentUser));

            //ACT
            userService.updateCurrentUserUsername("newUsername");
        }
        //ASSERT
        ArgumentCaptor<UserEntity> capturedUserEntity = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).saveAndFlush(capturedUserEntity.capture());
        assertEquals("newUsername", capturedUserEntity.getValue().getUsername());
    }

    @Test
    public void updateUserRoles_givenUserIdAndStringRoles_shouldUpdateUserRoles() {
        //ARRANGE
        Long userId = 1L;
        List<String> stringRoles = new ArrayList<>();
        stringRoles.add("USER");

        UserEntity userEntity = new UserEntity().setId(userId);
        UserRoleEntity roleEntity = new UserRoleEntity().setRole(UserRoleEnum.USER);
        List<UserRoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRoleRepository.findByRole(any())).thenReturn(roleEntity);

        //ACT
        userService.updateUserRoles(userId, stringRoles);

        //ASSERT
        ArgumentCaptor<UserEntity> capturedUserEntity = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(capturedUserEntity.capture());
        assertEquals(roleEntities, capturedUserEntity.getValue().getRoles());
    }
}
