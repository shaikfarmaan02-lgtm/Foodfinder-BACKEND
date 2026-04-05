package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodDonation entity operations.
 */
@Repository
public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {

    /**
     * Find all donations by donor ID.
     * @param donorId the donor's ID
     * @return list of food donations by that donor
     */
    List<FoodDonation> findByDonorId(Long donorId);

    /**
     * Find all donations by status.
     * @param status the status to filter by (AVAILABLE, CLAIMED)
     * @return list of food donations with that status
     */
    List<FoodDonation> findByStatus(DonationStatus status);

    /**
     * Find all donations by location.
     * @param location the location to filter by
     * @return list of food donations in that location
     */
    List<FoodDonation> findByLocation(String location);
}
