package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.FoodRequest;
import com.foodfinder.backend.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodRequest entity operations.
 */
@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Long> {

    /**
     * Find all food requests by donation ID.
     * @param donationId the donation ID
     * @return list of food requests for that donation
     */
    List<FoodRequest> findByDonationId(Long donationId);

    /**
     * Find all food requests by NGO ID.
     * @param ngoId the NGO ID
     * @return list of food requests from that NGO
     */
    List<FoodRequest> findByNgoId(Long ngoId);

    /**
     * Find food requests by status.
     * @param status the request status
     * @return list of food requests with that status
     */
    List<FoodRequest> findByStatus(RequestStatus status);

    /**
     * Find all pending food requests for a specific donation.
     * @param donationId the donation ID
     * @param status the status to filter by
     * @return list of food requests
     */
    List<FoodRequest> findByDonationIdAndStatus(Long donationId, RequestStatus status);
}
