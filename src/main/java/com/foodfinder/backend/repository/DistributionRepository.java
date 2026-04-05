package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.Distribution;
import com.foodfinder.backend.model.DistributionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Distribution entity operations.
 */
@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    /**
     * Find all distributions for a specific donation.
     * @param donationId the donation ID
     * @return list of distributions for that donation
     */
    List<Distribution> findByDonationId(Long donationId);

    /**
     * Find all distributions from a specific donor.
     * @param donorId the donor ID
     * @return list of distributions from that donor
     */
    List<Distribution> findByDonorId(Long donorId);

    /**
     * Find all distributions to a specific NGO.
     * @param ngoId the NGO ID
     * @return list of distributions to that NGO
     */
    List<Distribution> findByNgoId(Long ngoId);

    /**
     * Find distributions by status.
     * @param status the distribution status
     * @return list of distributions with that status
     */
    List<Distribution> findByStatus(DistributionStatus status);

    /**
     * Find distribution by food request ID.
     * @param foodRequestId the food request ID
     * @return list of distributions for that request
     */
    List<Distribution> findByFoodRequestId(Long foodRequestId);
}
