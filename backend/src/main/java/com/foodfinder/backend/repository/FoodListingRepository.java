package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodListing entity operations.
 */
@Repository
public interface FoodListingRepository extends JpaRepository<FoodListing, Long> {

    /**
     * Find all listings by donor ID.
     * @param donorId the donor's ID
     * @return list of food listings by that donor
     */
    List<FoodListing> findByDonorId(Long donorId);

    /**
     * Find all listings by status.
     * @param status the status to filter by (AVAILABLE, CLAIMED)
     * @return list of food listings with that status
     */
    List<FoodListing> findByStatus(DonationStatus status);

    /**
     * Find all listings by location.
     * @param location the location to filter by
     * @return list of food listings in that location
     */
    List<FoodListing> findByLocation(String location);
}
