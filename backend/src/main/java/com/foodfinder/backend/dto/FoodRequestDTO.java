package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for food request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDTO {
    private Long id;
    private Long donationId;
    private Long ngoId;
    private String ngoName;
    private String foodName;
    private RequestStatus status;
    private LocalDateTime requestTime;
    private LocalDateTime acceptedTime;
    private LocalDateTime collectedTime;
    private String rejectionReason;
}
