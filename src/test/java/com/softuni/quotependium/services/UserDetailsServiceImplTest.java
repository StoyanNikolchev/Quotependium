package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.entities.UserRoleEntity;
import com.softuni.quotependium.domain.enums.UserRoleEnum;
import com.softuni.quotependium.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_whenUserExists_returnUserDetails() {
        //ARRANGE
        String username = "Test User";
        String password = "password";

        UserRoleEntity roleEntity = new UserRoleEntity();
        roleEntity.setRole(UserRoleEnum.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRoles(List.of(roleEntity));

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));

        //ACT
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //ASSERT
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        //ARRANGE
        String username = "nonexistentuser";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        //ACT + ASSERT
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}