package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.Claim;
import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodListing;
import com.foodfinder.backend.model.Role;
import com.foodfinder.backend.model.User;
import com.foodfinder.backend.repository.ClaimRepository;
import com.foodfinder.backend.repository.FoodListingRepository;
import com.foodfinder.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for Claim-related business logic.
 */
@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FoodListingRepository foodListingRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new claim for a food donation.
     * @param claim the claim to create
     * @return the saved claim
     * @throws RuntimeException if donation already claimed or NGO not found
     */
    @Transactional
    public Claim createClaim(Claim claim) {
        // Verify donation exists
        FoodListing donation = foodListingRepository.findById(claim.getDonationId())
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + claim.getDonationId()));

        // Check if donation is already claimed
        if (donation.getStatus() == DonationStatus.CLAIMED) {
            throw new RuntimeException("Donation is already claimed");
        }

        // Verify NGO exists and has NGO role
        User ngo = userRepository.findById(claim.getNgoId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + claim.getNgoId()));

        if (ngo.getRole() != Role.NGO) {
            throw new RuntimeException("Only NGO users can claim donations");
        }

        // Update donation status to CLAIMED
        donation.setStatus(DonationStatus.CLAIMED);
        foodListingRepository.save(donation);

        // Save and return the claim
        return claimRepository.save(claim);
    }

    /**
     * Get all claims.
     * @return list of all claims
     */
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    /**
     * Get claim by ID.
     * @param id the claim ID
     * @return the claim
     * @throws ResourceNotFoundException if claim not found
     */
    public Claim getClaimById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
    }

    /**
     * Get all claims by NGO ID.
     * @param ngoId the NGO's ID
     * @return list of claims made by that NGO
     */
    public List<Claim> getClaimsByNgoId(Long ngoId) {
        return claimRepository.findByNgoId(ngoId);
    }

    /**
     * Get all claims by donation ID.
     * @param donationId the donation ID
     * @return list of claims for that donation
     */
    public List<Claim> getClaimsByDonationId(Long donationId) {
        return claimRepository.findByDonationId(donationId);
    }

    /**
     * Delete a claim by ID.
     * @param id the claim ID to delete
     * @throws ResourceNotFoundException if claim not found
     */
    @Transactional
    public void deleteClaim(Long id) {
        Claim claim = getClaimById(id);
        
        // Set donation status back to AVAILABLE
        FoodListing donation = foodListingRepository.findById(claim.getDonationId())
                .orElse(null);
        if (donation != null) {
            donation.setStatus(DonationStatus.AVAILABLE);
            foodListingRepository.save(donation);
        }
        
        claimRepository.deleteById(id);
    }

    /**
     * Count total number of claims.
     * @return total claim count
     */
    public long countClaims() {
        return claimRepository.count();
    }
}
