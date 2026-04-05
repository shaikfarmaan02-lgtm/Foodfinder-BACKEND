package com.foodfinder.backend.dto;

import com.foodfinder.backend.model.DistributionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for distribution.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionDTO {
    private Long id;
    private Long donationId;
    private Long foodRequestId;
    private Long donorId;
    private Long ngoId;
    private String donorName;
    private String ngoName;
    private String foodName;
    private DistributionStatus status;
    private LocalDateTime distributionDate;
    private LocalDateTime deliveredDate;
    private String distributionLocation;
    private String notes;
}
