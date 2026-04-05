package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.dto.LoginRequest;
import com.foodfinder.backend.dto.LoginResponse;
import com.foodfinder.backend.dto.RegisterRequest;
import com.foodfinder.backend.model.ApprovalStatus;
import com.foodfinder.backend.model.Role;
import com.foodfinder.backend.model.User;
import com.foodfinder.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User operations.
 * 
 * Endpoints:
 * - POST /api/users/register - Register a new user
 * - POST /api/users/login - User login
 * - GET /api/users - Get all users
 * - GET /api/users/{id} - Get user by ID
 * - PUT /api/users/{id} - Update user profile
 * - PUT /api/users/{id}/approve - Approve user (ADMIN only)
 * - PUT /api/users/{id}/reject - Reject user (ADMIN only)
 * - PUT /api/users/{id}/suspend - Suspend user (ADMIN only)
 * - DELETE /api/users/{id} - Delete user (ADMIN only)
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User savedUser = userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully. Please wait for admin approval.", savedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * User login.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.loginUser(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all users (ADMIN only).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update user profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Approve user account (ADMIN only).
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<User>> approveUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            // In production, validate that adminId is an actual admin
            User approvedUser = userService.approveUser(id);
            return ResponseEntity.ok(ApiResponse.success("User approved successfully", approvedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Reject user account (ADMIN only).
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<User>> rejectUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            // In production, validate that adminId is an actual admin
            User rejectedUser = userService.rejectUser(id);
            return ResponseEntity.ok(ApiResponse.success("User rejected successfully", rejectedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Suspend user account (ADMIN only).
     */
    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<User>> suspendUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            // In production, validate that adminId is an actual admin
            User suspendedUser = userService.suspendUser(id);
            return ResponseEntity.ok(ApiResponse.success("User suspended successfully", suspendedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete user by ID (ADMIN only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            // In production, validate that adminId is an actual admin
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get users by role.
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable Role role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get users by approval status.
     */
    @GetMapping("/approval/{status}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByApprovalStatus(@PathVariable ApprovalStatus status) {
        List<User> users = userService.getUsersByApprovalStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get total user count.
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = userService.countUsers();
        return ResponseEntity.ok(ApiResponse.success("User count retrieved successfully", count));
    }
}
