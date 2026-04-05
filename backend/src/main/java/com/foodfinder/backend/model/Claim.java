package com.foodfinder.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Claim entity representing when an NGO claims a food donation.
 */
@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Donation ID is required")
    @Column(nullable = false)
    private Long donationId;

    @NotNull(message = "NGO ID is required")
    @Column(nullable = false)
    private Long ngoId;

    @Column(nullable = false)
    private LocalDateTime claimTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donationId", insertable = false, updatable = false)
    private FoodDonation donation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ngoId", insertable = false, updatable = false)
    private User ngo;

    @PrePersist
    protected void onCreate() {
        this.claimTime = LocalDateTime.now();
    }
}
