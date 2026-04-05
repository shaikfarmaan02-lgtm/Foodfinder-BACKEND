package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.ApprovalStatus;
import com.foodfinder.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private ApprovalStatus approvalStatus;
    private String message;
    private boolean canAccess;
}
