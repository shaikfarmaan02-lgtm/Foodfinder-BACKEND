package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String phone;
    private String address;
    private String organization;
    private String city;
    private String state;
    private String pincode;
}
