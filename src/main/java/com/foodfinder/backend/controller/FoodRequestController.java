package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.dto.CreateFoodRequestRequest;
import com.foodfinder.backend.model.FoodRequest;
import com.foodfinder.backend.service.FoodRequestService;
import com.foodfinder.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Food Request operations (NGO requests for food).
 * 
 * Endpoints:
 * - POST /api/request - Create a new food request (NGO only)
 * - GET /api/request - Get all food requests
 * - GET /api/request/{id} - Get request by ID
 * - PUT /api/request/{id}/accept - Accept request (DONOR only)
 * - PUT /api/request/{id}/reject - Reject request (DONOR only)
 * - PUT /api/request/{id}/collect - Mark as collected (NGO only)
 * - GET /api/request/ngo/{ngoId} - Get requests by NGO
 * - GET /api/request/donation/{donationId} - Get requests for donation
 */
@RestController
@RequestMapping("/api/request")
@CrossOrigin(origins = "*")
public class FoodRequestController {

    @Autowired
    private FoodRequestService foodRequestService;

    @Autowired
    private UserService userService;

    /**
     * Create a new food request (NGO only).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FoodRequest>> createRequest(
            @Valid @RequestBody CreateFoodRequestRequest requestDto,
            @RequestHeader(value = "X-User-Id", required = false) Long ngoId) {
        try {
            // Validate if user is approved NGO
            if (ngoId != null && !userService.validateNGO(ngoId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved NGOs can request food"));
            }

            // Create entity from DTO and set ngoId
            FoodRequest foodRequest = new FoodRequest();
            foodRequest.setDonationId(requestDto.getDonationId());
            foodRequest.setNgoId(ngoId);
            foodRequest.setRequestTime(java.time.LocalDateTime.now());
            
            FoodRequest createdRequest = foodRequestService.createRequest(foodRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Food request created successfully", createdRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all food requests.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodRequest>>> getAllRequests() {
        List<FoodRequest> requests = foodRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success("Food requests retrieved successfully", requests));
    }

    /**
     * Get request by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodRequest>> getRequestById(@PathVariable Long id) {
        try {
            FoodRequest request = foodRequestService.getRequestById(id);
            return ResponseEntity.ok(ApiResponse.success("Food request retrieved successfully", request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Accept a food request (DONOR only).
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<FoodRequest>> acceptRequest(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long donorId) {
        try {
            // Validate if user is approved donor
            if (donorId != null && !userService.validateDonor(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved donors can accept food requests"));
            }

            FoodRequest acceptedRequest = foodRequestService.acceptRequest(id);
            return ResponseEntity.ok(ApiResponse.success("Food request accepted successfully", acceptedRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Reject a food request (DONOR only).
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<FoodRequest>> rejectRequest(
            @PathVariable Long id,
            @RequestParam String rejectionReason,
            @RequestHeader(value = "X-User-Id", required = false) Long donorId) {
        try {
            // Validate if user is approved donor
            if (donorId != null && !userService.validateDonor(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved donors can reject food requests"));
            }

            FoodRequest rejectedRequest = foodRequestService.rejectRequest(id, rejectionReason);
            return ResponseEntity.ok(ApiResponse.success("Food request rejected successfully", rejectedRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Mark food request as collected (NGO only).
     */
    @PutMapping("/{id}/collect")
    public ResponseEntity<ApiResponse<FoodRequest>> collectFood(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long ngoId) {
        try {
            // Validate if user is approved NGO
            if (ngoId != null && !userService.validateNGO(ngoId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved NGOs can collect food"));
            }

            FoodRequest collectedRequest = foodRequestService.collectFood(id);
            return ResponseEntity.ok(ApiResponse.success("Food marked as collected successfully", collectedRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get requests by NGO ID.
     */
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<ApiResponse<List<FoodRequest>>> getRequestsByNgo(@PathVariable Long ngoId) {
        List<FoodRequest> requests = foodRequestService.getRequestsByNgoId(ngoId);
        return ResponseEntity.ok(ApiResponse.success("NGO's food requests retrieved successfully", requests));
    }

    /**
     * Get pending requests for a donation.
     */
    @GetMapping("/donation/{donationId}/pending")
    public ResponseEntity<ApiResponse<List<FoodRequest>>> getPendingRequests(@PathVariable Long donationId) {
        List<FoodRequest> requests = foodRequestService.getPendingRequests(donationId);
        return ResponseEntity.ok(ApiResponse.success("Pending food requests retrieved successfully", requests));
    }

    /**
     * Get all requests for a specific donation.
     */
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<ApiResponse<List<FoodRequest>>> getRequestsByDonation(@PathVariable Long donationId) {
        List<FoodRequest> requests = foodRequestService.getRequestsByDonationId(donationId);
        return ResponseEntity.ok(ApiResponse.success("Food requests for donation retrieved successfully", requests));
    }

    /**
     * Delete a food request.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRequest(@PathVariable Long id) {
        try {
            foodRequestService.deleteRequest(id);
            return ResponseEntity.ok(ApiResponse.success("Food request deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
