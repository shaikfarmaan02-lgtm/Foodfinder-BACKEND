package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.dto.FoodListingRequest;
import com.foodfinder.backend.model.DonationStatus;
import com.foodfinder.backend.model.FoodListing;
import com.foodfinder.backend.service.FoodListingService;
import com.foodfinder.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Food Listing operations.
 * 
 * Endpoints:
 * - POST /api/food - Add a new food listing (DONOR only)
 * - GET /api/food - Get all listings
 * - GET /api/food/{id} - Get listing by ID
 * - PUT /api/food/{id} - Update listing (DONOR only)
 * - DELETE /api/food/{id} - Delete listing (DONOR only)
 * - GET /api/food/available - Get available listings
 * - GET /api/food/donor/{donorId} - Get listings by donor
 * - GET /api/food/status/{status} - Get listings by status
 */
@RestController
@RequestMapping("/api/food")
@CrossOrigin(origins = "*")
public class FoodListingController {

    @Autowired
    private FoodListingService foodListingService;

    @Autowired
    private UserService userService;

    /**
     * Add a new food listing (DONOR only).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FoodListing>> addDonation(
            @Valid @RequestBody FoodListingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long donorId) {
        try {
            // Validate if user is approved donor
            if (donorId != null && !userService.validateDonor(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved donors can create food listings"));
            }

            // Create entity from DTO and set donorId
            FoodListing foodListing = new FoodListing();
            foodListing.setFoodName(request.getFoodName());
            foodListing.setQuantity(request.getQuantity());
            foodListing.setLocation(request.getLocation());
            foodListing.setExpiryTime(request.getExpiryTime());
            foodListing.setStatus(request.getStatus() != null ? request.getStatus() : DonationStatus.AVAILABLE);
            foodListing.setDonorId(donorId);
            foodListing.setCreatedAt(LocalDateTime.now());
            
            FoodListing savedListing = foodListingService.addDonation(foodListing);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Food listing created successfully", savedListing));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all food listings.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodListing>>> getAllDonations() {
        List<FoodListing> donations = foodListingService.getAllDonations();
        return ResponseEntity.ok(ApiResponse.success("Food listings retrieved successfully", donations));
    }

    /**
     * Get listing by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodListing>> getDonationById(@PathVariable Long id) {
        try {
            FoodListing donation = foodListingService.getDonationById(id);
            return ResponseEntity.ok(ApiResponse.success("Food listing retrieved successfully", donation));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update food listing (DONOR only).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodListing>> updateDonation(
            @PathVariable Long id,
            @Valid @RequestBody FoodListing foodListing,
            @RequestHeader(value = "X-User-Id", required = false) Long donorId) {
        try {
            FoodListing existingListing = foodListingService.getDonationById(id);
            
            // Validate if user is approved donor and owner
            if (donorId != null && !userService.validateDonor(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved donors can update food listings"));
            }
            
            if (donorId != null && !existingListing.getDonorId().equals(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("You can only update your own food listings"));
            }

            FoodListing updatedListing = foodListingService.updateDonation(id, foodListing);
            return ResponseEntity.ok(ApiResponse.success("Food listing updated successfully", updatedListing));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete listing (DONOR only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDonation(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long donorId) {
        try {
            FoodListing existingListing = foodListingService.getDonationById(id);
            
            // Validate if user is approved donor and owner
            if (donorId != null && !userService.validateDonor(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Only approved donors can delete food listings"));
            }
            
            if (donorId != null && !existingListing.getDonorId().equals(donorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("You can only delete your own food listings"));
            }

            foodListingService.deleteDonation(id);
            return ResponseEntity.ok(ApiResponse.success("Food listing deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all available listings.
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<FoodListing>>> getAvailableDonations() {
        List<FoodListing> donations = foodListingService.getAvailableDonations();
        return ResponseEntity.ok(ApiResponse.success("Available food listings retrieved successfully", donations));
    }

    /**
     * Get listings by donor ID.
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<ApiResponse<List<FoodListing>>> getDonationsByDonor(@PathVariable Long donorId) {
        List<FoodListing> donations = foodListingService.getDonationsByDonorId(donorId);
        return ResponseEntity.ok(ApiResponse.success("Donor's food listings retrieved successfully", donations));
    }

    /**
     * Get listings by status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<FoodListing>>> getDonationsByStatus(@PathVariable DonationStatus status) {
        List<FoodListing> donations = foodListingService.getDonationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Food listings retrieved successfully", donations));
    }
}
