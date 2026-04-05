package com.foodfinder.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for feedback.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Long id;
    private Long userId;
    private Long donationId;
    private String userName;
    private String foodName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
