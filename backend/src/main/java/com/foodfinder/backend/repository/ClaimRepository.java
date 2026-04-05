package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Claim entity operations.
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    /**
     * Find all claims by NGO ID.
     * @param ngoId the NGO's ID
     * @return list of claims made by that NGO
     */
    List<Claim> findByNgoId(Long ngoId);

    /**
     * Find all claims by donation ID.
     * @param donationId the donation ID
     * @return list of claims for that donation
     */
    List<Claim> findByDonationId(Long donationId);

    /**
     * Check if a donation has already been claimed.
     * @param donationId the donation ID to check
     * @return true if the donation has been claimed
     */
    boolean existsByDonationId(Long donationId);

    /**
     * Find claim by donation ID.
     * @param donationId the donation ID
     * @return Optional containing the claim if found
     */
    Optional<Claim> findFirstByDonationId(Long donationId);
}
