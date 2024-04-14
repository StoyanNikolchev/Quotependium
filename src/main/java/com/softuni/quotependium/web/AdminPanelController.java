package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.ManageUserRolesDto;
import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.softuni.quotependium.domain.enums.Constants.NO_ROLES_SELECTED_ERROR;
import static com.softuni.quotependium.domain.enums.Constants.ROLES_UPDATED_SUCCESSFULLY;

@Controller
public class AdminPanelController {
    private final UserService userService;

    @Autowired
    public AdminPanelController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model,
                           @PageableDefault(
                                   sort = "id",
                                   direction = Sort.Direction.ASC,
                                   size = 10,
                                   page = 0)
                           Pageable pageable) {

        Page<ManageUserRolesDto> users = this.userService.getAllUsersToManageDto(pageable);
        model.addAttribute("usersPage", users);
        return "admin-panel";
    }

    @PostMapping("/admin/users")
    public String updateUserRoles(@RequestParam("userId") Long userId,
                                  @RequestParam(value = "userRoles", required = false) List<String> userRoles,
                                  RedirectAttributes redirectAttributes) {

        if (userRoles == null) {
            redirectAttributes.addFlashAttribute("failureMessage", NO_ROLES_SELECTED_ERROR);
            return "redirect:/admin/users";
        }

        this.userService.updateUserRoles(userId, userRoles);
        redirectAttributes.addFlashAttribute("successMessage", ROLES_UPDATED_SUCCESSFULLY);
        return "redirect:/admin/users";
    }
}
