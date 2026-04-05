package com.foodfinder.backend.dto;

import lombok.Data;

/**
 * DTO for login request.
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
