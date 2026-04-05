package com.foodfinder.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FoodListing entity representing food items donated by users.
 */
@Entity
@Table(name = "food_listing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name is required")
    @Column(nullable = false)
    private String foodName;

    @NotBlank(message = "Quantity is required")
    @Column(nullable = false)
    private String quantity;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Column
    private LocalDateTime expiryTime;

    @JsonProperty("donorId")
    @Column(nullable = false)
    private Long donorId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status = DonationStatus.AVAILABLE;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donorId", insertable = false, updatable = false)
    private User donor;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = DonationStatus.AVAILABLE;
        }
    }
}
