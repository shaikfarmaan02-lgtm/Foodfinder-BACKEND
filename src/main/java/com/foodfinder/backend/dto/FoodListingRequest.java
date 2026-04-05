package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.DonationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating/updating food listings.
 * Note: donorId is set by the controller from the request header.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodListingRequest {
    
    @NotBlank(message = "Food name is required")
    private String foodName;

    @NotBlank(message = "Quantity is required")
    private String quantity;

    @NotBlank(message = "Location is required")
    private String location;

    private LocalDateTime expiryTime;

    @NotNull(message = "Status is required")
    private DonationStatus status = DonationStatus.AVAILABLE;
}
