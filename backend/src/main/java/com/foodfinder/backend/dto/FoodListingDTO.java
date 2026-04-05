package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for food listing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodListingDTO {
    private Long id;
    private String foodName;
    private String quantity;
    private String location;
    private LocalDateTime expiryTime;
    private Long donorId;
    private String donorName;
    private DonationStatus status;
    private LocalDateTime createdAt;
}
