package com.foodfinder.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for contact message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String phone;
    private LocalDateTime createdAt;
    private Boolean resolved;
    private String adminNotes;
}
