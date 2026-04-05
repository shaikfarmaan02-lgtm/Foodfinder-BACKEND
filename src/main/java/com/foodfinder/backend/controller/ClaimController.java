package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.model.Claim;
import com.foodfinder.backend.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Claim operations.
 * 
 * Endpoints:
 * - POST /api/claims - Create a new claim
 * - GET /api/claims - Get all claims
 * - GET /api/claims/{id} - Get claim by ID
 * - GET /api/claims/ngo/{ngoId} - Get claims by NGO ID
 * - DELETE /api/claims/{id} - Delete claim by ID
 */
@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    /**
     * Create a new claim for a food donation.
     * 
     * Request Body:
     * {
     *   "donationId": 1,
     *   "ngoId": 2
     * }
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claim created successfully",
     *   "data": {
     *     "id": 1,
     *     "donationId": 1,
     *     "ngoId": 2,
     *     "claimTime": "2024-01-15T10:30:00"
     *   }
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Claim>> createClaim(@Valid @RequestBody Claim claim) {
        Claim savedClaim = claimService.createClaim(claim);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Claim created successfully", savedClaim));
    }

    /**
     * Get all claims.
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claims retrieved successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "donationId": 1,
     *       "ngoId": 2,
     *       "claimTime": "2024-01-15T10:30:00",
     *       "donation": { ... },
     *       "ngo": { ... }
     *     }
     *   ]
     * }
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Claim>>> getAllClaims() {
        List<Claim> claims = claimService.getAllClaims();
        return ResponseEntity.ok(ApiResponse.success("Claims retrieved successfully", claims));
    }

    /**
     * Get claim by ID.
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claim retrieved successfully",
     *   "data": {
     *     "id": 1,
     *     "donationId": 1,
     *     "ngoId": 2,
     *     "claimTime": "2024-01-15T10:30:00",
     *     "donation": { ... },
     *     "ngo": { ... }
     *   }
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Claim>> getClaimById(@PathVariable Long id) {
        Claim claim = claimService.getClaimById(id);
        return ResponseEntity.ok(ApiResponse.success("Claim retrieved successfully", claim));
    }

    /**
     * Get claims by NGO ID.
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claims retrieved successfully for NGO",
     *   "data": [...]
     * }
     */
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<ApiResponse<List<Claim>>> getClaimsByNgo(@PathVariable Long ngoId) {
        List<Claim> claims = claimService.getClaimsByNgoId(ngoId);
        return ResponseEntity.ok(ApiResponse.success("Claims retrieved successfully for NGO", claims));
    }

    /**
     * Delete claim by ID.
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claim deleted successfully",
     *   "data": null
     * }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.ok(ApiResponse.success("Claim deleted successfully", null));
    }

    /**
     * Get total claim count.
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Claim count retrieved successfully",
     *   "data": 15
     * }
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getClaimCount() {
        long count = claimService.countClaims();
        return ResponseEntity.ok(ApiResponse.success("Claim count retrieved successfully", count));
    }
}
