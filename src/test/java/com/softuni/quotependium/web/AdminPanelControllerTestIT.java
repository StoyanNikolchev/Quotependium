package com.softuni.quotependium.web;

import com.softuni.quotependium.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "AdminUser", roles = {"ADMINISTRATOR", "USER"})
public class AdminPanelControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUsersPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(userService.getAllUsersToManageDto(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usersPage"))
                .andExpect(model().attribute("usersPage", new PageImpl<>(Collections.emptyList())));
    }

    @Test
    public void testUpdateUserRolesSuccess() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).updateUserRoles(userId, Collections.singletonList("ROLE_ADMIN"));
        mockMvc.perform(post("/admin/users")
                        .with(csrf())
                        .param("userId", userId.toString())
                        .param("userRoles", "ROLE_ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testUpdateUserRolesNoRolesSelected() throws Exception {
        Long userId = 1L;
        mockMvc.perform(post("/admin/users")
                        .with(csrf())
                        .param("userId", userId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("failureMessage"));
    }

}
