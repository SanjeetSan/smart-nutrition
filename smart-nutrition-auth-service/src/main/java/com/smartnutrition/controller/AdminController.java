package com.smartnutrition.controller;

import com.smartnutrition.dto.request.UpdateUserRoleRequest;
import com.smartnutrition.dto.response.AdminUserResponse;
import com.smartnutrition.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Operations", description = "Privileged management operations for developers and school administrators")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    @Operation(summary = "List all users", description = "Retrieves all registered parents, teachers, and administrators in the system.")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/roles")
    @Operation(summary = "Update user role", description = "Promotes or changes a user role (e.g. promoting a teacher to ADMIN / School Management).")
    public ResponseEntity<AdminUserResponse> updateUserRole(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(id, request));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Deactivate/Delete user", description = "Deactivates a user account in the system.")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }

    @GetMapping("/system/health")
    @Operation(summary = "System health and metrics", description = "Returns system diagnostics for debugging and administrative monitoring.")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        return ResponseEntity.ok(adminService.getSystemHealth());
    }
}
