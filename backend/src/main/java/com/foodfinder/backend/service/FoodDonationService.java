package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodDonation;
import com.foodfinder.backend.repository.FoodDonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for FoodDonation-related business logic.
 */
@Service
public class FoodDonationService {

    @Autowired
    private FoodDonationRepository foodDonationRepository;

    /**
     * Add a new food donation.
     * @param foodDonation the food donation to add
     * @return the saved food donation
     */
    public FoodDonation addDonation(FoodDonation foodDonation) {
        return foodDonationRepository.save(foodDonation);
    }

    /**
     * Get all food donations.
     * @return list of all food donations
     */
    public List<FoodDonation> getAllDonations() {
        return foodDonationRepository.findAll();
    }

    /**
     * Get food donation by ID.
     * @param id the donation ID
     * @return the donation
     * @throws ResourceNotFoundException if donation not found
     */
    public FoodDonation getDonationById(Long id) {
        return foodDonationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + id));
    }

    /**
     * Get all donations by donor ID.
     * @param donorId the donor's ID
     * @return list of food donations by that donor
     */
    public List<FoodDonation> getDonationsByDonorId(Long donorId) {
        return foodDonationRepository.findByDonorId(donorId);
    }

    /**
     * Get all donations by status.
     * @param status the status to filter by
     * @return list of food donations with that status
     */
    public List<FoodDonation> getDonationsByStatus(DonationStatus status) {
        return foodDonationRepository.findByStatus(status);
    }

    /**
     * Get all available donations.
     * @return list of available food donations
     */
    public List<FoodDonation> getAvailableDonations() {
        return foodDonationRepository.findByStatus(DonationStatus.AVAILABLE);
    }

    /**
     * Delete a food donation by ID.
     * @param id the donation ID to delete
     * @throws ResourceNotFoundException if donation not found
     */
    public void deleteDonation(Long id) {
        if (!foodDonationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donation not found with id: " + id);
        }
        foodDonationRepository.deleteById(id);
    }

    /**
     * Update donation status.
     * @param id the donation ID
     * @param status the new status
     * @return the updated donation
     */
    public FoodDonation updateDonationStatus(Long id, DonationStatus status) {
        FoodDonation donation = getDonationById(id);
        donation.setStatus(status);
        return foodDonationRepository.save(donation);
    }

    /**
     * Update a food donation.
     * @param id the donation ID
     * @param foodDonation the updated food donation data
     * @return the updated donation
     */
    public FoodDonation updateDonation(Long id, FoodDonation foodDonation) {
        FoodDonation existingDonation = getDonationById(id);
        existingDonation.setFoodName(foodDonation.getFoodName() != null ? foodDonation.getFoodName() : existingDonation.getFoodName());
        existingDonation.setQuantity(foodDonation.getQuantity() != null ? foodDonation.getQuantity() : existingDonation.getQuantity());
        existingDonation.setLocation(foodDonation.getLocation() != null ? foodDonation.getLocation() : existingDonation.getLocation());
        existingDonation.setExpiryTime(foodDonation.getExpiryTime() != null ? foodDonation.getExpiryTime() : existingDonation.getExpiryTime());
        if (foodDonation.getStatus() != null) {
            existingDonation.setStatus(foodDonation.getStatus());
        }
        return foodDonationRepository.save(existingDonation);
    }

    /**
     * Count total number of donations.
     * @return total donation count
     */
    public long countDonations() {
        return foodDonationRepository.count();
    }
}
