package com.foodfinder.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Distribution entity representing the distribution of food from donor to NGO.
 */
@Entity
@Table(name = "distributions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Donation ID is required")
    @Column(nullable = false)
    private Long donationId;

    @NotNull(message = "Food Request ID is required")
    @Column(nullable = false)
    private Long foodRequestId;

    @NotNull(message = "Donor ID is required")
    @Column(nullable = false)
    private Long donorId;

    @NotNull(message = "NGO ID is required")
    @Column(nullable = false)
    private Long ngoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistributionStatus status = DistributionStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime distributionDate;

    @Column
    private LocalDateTime deliveredDate;

    @Column
    private String distributionLocation;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donationId", insertable = false, updatable = false)
    private FoodListing donation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "foodRequestId", insertable = false, updatable = false)
    private FoodRequest foodRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donorId", insertable = false, updatable = false)
    private User donor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ngoId", insertable = false, updatable = false)
    private User ngo;
}
