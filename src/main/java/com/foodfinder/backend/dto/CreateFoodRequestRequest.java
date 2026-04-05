package com.foodfinder.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a food request.
 * Note: ngoId comes from X-User-Id header, not from request body.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodRequestRequest {

    @NotNull(message = "Donation ID is required")
    private Long donationId;

    @NotNull(message = "Quantity requested is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantityRequested;

    private String message;
}
