package com.softuni.quotependium.web;

import com.softuni.quotependium.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testGetRegister() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testPostRegisterValidData() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "testUser")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .param("fullName", "fullName")
                        .param("confirmPassword", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testPostRegisterInvalidData() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "  ")
                        .param("email", "badEmail")
                        .param("password", "willItMatch?")
                        .param("confirmPassword", "nope")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterFormDto"))
                .andExpect(model().attributeHasFieldErrors("userRegisterFormDto", "username"))
                .andExpect(model().attributeHasFieldErrors("userRegisterFormDto", "email"))
                .andExpect(model().attributeHasFieldErrors("userRegisterFormDto", "confirmPassword"));
    }

    @Test
    @WithMockUser(username = "Pesho")
    public void testLogout() throws Exception {
        mockMvc.perform(get("/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }
}
