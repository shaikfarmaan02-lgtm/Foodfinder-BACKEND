package com.foodfinder.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FoodRequest entity representing when an NGO requests a food donation.
 */
@Entity
@Table(name = "food_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Donation ID is required")
    @Column(nullable = false)
    private Long donationId;

    @NotNull(message = "NGO ID is required")
    @Column(nullable = false)
    private Long ngoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    @Column
    private LocalDateTime acceptedTime;

    @Column
    private LocalDateTime collectedTime;

    @Column
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donationId", insertable = false, updatable = false)
    private FoodListing donation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ngoId", insertable = false, updatable = false)
    private User ngo;
}
