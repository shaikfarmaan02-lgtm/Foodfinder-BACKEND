package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodListing;
import com.foodfinder.backend.repository.FoodListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for FoodListing-related business logic.
 */
@Service
public class FoodListingService {

    @Autowired
    private FoodListingRepository foodListingRepository;

    /**
     * Add a new food listing.
     * @param foodListing the food listing to add
     * @return the saved food listing
     */
    public FoodListing addDonation(FoodListing foodListing) {
        return foodListingRepository.save(foodListing);
    }

    /**
     * Get all food listings.
     * @return list of all food listings
     */
    public List<FoodListing> getAllDonations() {
        return foodListingRepository.findAll();
    }

    /**
     * Get food listing by ID.
     * @param id the listing ID
     * @return the listing
     * @throws ResourceNotFoundException if listing not found
     */
    public FoodListing getDonationById(Long id) {
        return foodListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + id));
    }

    /**
     * Get all listings by donor ID.
     * @param donorId the donor's ID
     * @return list of food listings by that donor
     */
    public List<FoodListing> getDonationsByDonorId(Long donorId) {
        return foodListingRepository.findByDonorId(donorId);
    }

    /**
     * Get all listings by status.
     * @param status the status to filter by
     * @return list of food listings with that status
     */
    public List<FoodListing> getDonationsByStatus(DonationStatus status) {
        return foodListingRepository.findByStatus(status);
    }

    /**
     * Get all available listings.
     * @return list of available food listings
     */
    public List<FoodListing> getAvailableDonations() {
        return foodListingRepository.findByStatus(DonationStatus.AVAILABLE);
    }

    /**
     * Delete a food listing by ID.
     * @param id the listing ID to delete
     * @throws ResourceNotFoundException if listing not found
     */
    public void deleteDonation(Long id) {
        if (!foodListingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found with id: " + id);
        }
        foodListingRepository.deleteById(id);
    }

    /**
     * Update listing status.
     * @param id the listing ID
     * @param status the new status
     * @return the updated listing
     */
    public FoodListing updateDonationStatus(Long id, DonationStatus status) {
        FoodListing donation = getDonationById(id);
        donation.setStatus(status);
        return foodListingRepository.save(donation);
    }

    /**
     * Update a food listing.
     * @param id the listing ID
     * @param foodListing the updated food listing data
     * @return the updated listing
     */
    public FoodListing updateDonation(Long id, FoodListing foodListing) {
        FoodListing existingDonation = getDonationById(id);
        existingDonation.setFoodName(foodListing.getFoodName() != null ? foodListing.getFoodName() : existingDonation.getFoodName());
        existingDonation.setQuantity(foodListing.getQuantity() != null ? foodListing.getQuantity() : existingDonation.getQuantity());
        existingDonation.setLocation(foodListing.getLocation() != null ? foodListing.getLocation() : existingDonation.getLocation());
        existingDonation.setExpiryTime(foodListing.getExpiryTime() != null ? foodListing.getExpiryTime() : existingDonation.getExpiryTime());
        if (foodListing.getStatus() != null) {
            existingDonation.setStatus(foodListing.getStatus());
        }
        return foodListingRepository.save(existingDonation);
    }

    /**
     * Count total number of listings.
     * @return total listing count
     */
    public long countDonations() {
        return foodListingRepository.count();
    }
}
