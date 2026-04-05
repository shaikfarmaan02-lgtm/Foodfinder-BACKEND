package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodListing;
import com.foodfinder.backend.model.FoodRequest;
import com.foodfinder.backend.model.RequestStatus;
import com.foodfinder.backend.repository.FoodListingRepository;
import com.foodfinder.backend.repository.FoodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for FoodRequest-related business logic.
 */
@Service
public class FoodRequestService {

    @Autowired
    private FoodRequestRepository foodRequestRepository;

    @Autowired
    private FoodListingRepository foodListingRepository;

    /**
     * Create a new food request (NGO only).
     * @param foodRequest the food request to create
     * @return the saved food request
     */
    public FoodRequest createRequest(FoodRequest foodRequest) {
        foodRequest.setRequestTime(LocalDateTime.now());
        foodRequest.setStatus(RequestStatus.PENDING);
        return foodRequestRepository.save(foodRequest);
    }

    /**
     * Get all food requests.
     * @return list of all food requests
     */
    public List<FoodRequest> getAllRequests() {
        return foodRequestRepository.findAll();
    }

    /**
     * Get food request by ID.
     * @param id the request ID
     * @return the request
     * @throws ResourceNotFoundException if request not found
     */
    public FoodRequest getRequestById(Long id) {
        return foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));
    }

    /**
     * Get food requests for a specific donation.
     * @param donationId the donation ID
     * @return list of food requests for that donation
     */
    public List<FoodRequest> getRequestsByDonationId(Long donationId) {
        return foodRequestRepository.findByDonationId(donationId);
    }

    /**
     * Get food requests from a specific NGO.
     * @param ngoId the NGO ID
     * @return list of food requests from that NGO
     */
    public List<FoodRequest> getRequestsByNgoId(Long ngoId) {
        return foodRequestRepository.findByNgoId(ngoId);
    }

    /**
     * Get pending requests for a specific donation.
     * @param donationId the donation ID
     * @return list of pending food requests
     */
    public List<FoodRequest> getPendingRequests(Long donationId) {
        return foodRequestRepository.findByDonationIdAndStatus(donationId, RequestStatus.PENDING);
    }

    /**
     * Accept a food request (Donor accepts NGO's request).
     * @param requestId the request ID to accept
     * @return the updated request
     */
    public FoodRequest acceptRequest(Long requestId) {
        FoodRequest request = getRequestById(requestId);
        request.setStatus(RequestStatus.ACCEPTED);
        request.setAcceptedTime(LocalDateTime.now());

        // Update the donation status to CLAIMED
        FoodListing donation = foodListingRepository.findById(request.getDonationId())
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        donation.setStatus(DonationStatus.CLAIMED);
        foodListingRepository.save(donation);

        return foodRequestRepository.save(request);
    }

    /**
     * Reject a food request (Donor rejects NGO's request).
     * @param requestId the request ID to reject
     * @param rejectionReason the reason for rejection
     * @return the updated request
     */
    public FoodRequest rejectRequest(Long requestId, String rejectionReason) {
        FoodRequest request = getRequestById(requestId);
        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejectionReason);
        return foodRequestRepository.save(request);
    }

    /**
     * Mark a food request as collected.
     * @param requestId the request ID
     * @return the updated request
     */
    public FoodRequest collectFood(Long requestId) {
        FoodRequest request = getRequestById(requestId);
        request.setStatus(RequestStatus.COLLECTED);
        request.setCollectedTime(LocalDateTime.now());
        return foodRequestRepository.save(request);
    }

    /**
     * Delete a food request.
     * @param id the request ID to delete
     */
    public void deleteRequest(Long id) {
        if (!foodRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Food request not found with id: " + id);
        }
        foodRequestRepository.deleteById(id);
    }

    /**
     * Count total number of food requests.
     * @return total request count
     */
    public long countRequests() {
        return foodRequestRepository.count();
    }
}
