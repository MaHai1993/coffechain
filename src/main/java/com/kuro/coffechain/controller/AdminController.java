package com.kuro.coffechain.controller;

import com.kuro.coffechain.dto.UserDTO;
import com.kuro.coffechain.service.AdminService;
import com.kuro.coffechain.service.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin", description = "Admin Dashboard APIs")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get Admin Dashboard", description = "Returns the admin dashboard data")
    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "Admin Dashboard Accessed";
    }

    @Operation(summary = "Get all users", description = "Returns all the users")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }
}
