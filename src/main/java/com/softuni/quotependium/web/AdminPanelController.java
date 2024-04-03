package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.ManageUserRolesDto;
import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminPanelController {
    private final int pageSize = 10;
    private final Sort sort = Sort.by("id").ascending();
    private final UserService userService;

    @Autowired
    public AdminPanelController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users/{pageNumber}")
    public String getUsers(@PathVariable int pageNumber, Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ManageUserRolesDto> users = this.userService.getAllUsersToManageDto(pageable);
        model.addAttribute("usersPage", users);
        return "admin-panel";
    }

    @PostMapping("/admin/users")
    public String updateUserRoles(@RequestParam("userId") Long userId,
                                  @RequestParam(value = "userRoles", required = false) List<String> userRoles,
                                  RedirectAttributes redirectAttributes) {

        if (userRoles == null) {
            redirectAttributes.addFlashAttribute("failureMessage", "Please select at least one role!");
            return "redirect:/admin/users/0";
        }

        this.userService.updateUserRoles(userId, userRoles);
        redirectAttributes.addFlashAttribute("successMessage", "Roles updated successfully");
        return "redirect:/admin/users/0";
    }
}
