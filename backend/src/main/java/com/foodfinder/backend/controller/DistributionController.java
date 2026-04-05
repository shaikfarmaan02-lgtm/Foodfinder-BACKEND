package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.model.Distribution;
import com.foodfinder.backend.model.DistributionStatus;
import com.foodfinder.backend.service.DistributionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Distribution operations.
 * 
 * Endpoints:
 * - POST /api/distribution - Create a new distribution
 * - GET /api/distribution - Get all distributions
 * - GET /api/distribution/{id} - Get distribution by ID
 * - PUT /api/distribution/{id}/start - Start distribution (IN_TRANSIT)
 * - PUT /api/distribution/{id}/deliver - Mark as delivered
 * - PUT /api/distribution/{id}/cancel - Cancel distribution
 * - GET /api/distribution/donor/{donorId} - Get distributions from donor
 * - GET /api/distribution/ngo/{ngoId} - Get distributions to NGO
 * - GET /api/distribution/status/{status} - Get by status
 */
@RestController
@RequestMapping("/api/distribution")
@CrossOrigin(origins = "*")
public class DistributionController {

    @Autowired
    private DistributionService distributionService;

    /**
     * Create a new distribution.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Distribution>> createDistribution(
            @Valid @RequestBody Distribution distribution) {
        try {
            Distribution createdDistribution = distributionService.createDistribution(distribution);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Distribution created successfully", createdDistribution));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all distributions.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Distribution>>> getAllDistributions() {
        List<Distribution> distributions = distributionService.getAllDistributions();
        return ResponseEntity.ok(ApiResponse.success("Distributions retrieved successfully", distributions));
    }

    /**
     * Get distribution by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Distribution>> getDistributionById(@PathVariable Long id) {
        try {
            Distribution distribution = distributionService.getDistributionById(id);
            return ResponseEntity.ok(ApiResponse.success("Distribution retrieved successfully", distribution));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Start distribution (IN_TRANSIT).
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<ApiResponse<Distribution>> startDistribution(@PathVariable Long id) {
        try {
            Distribution distribution = distributionService.startDistribution(id);
            return ResponseEntity.ok(ApiResponse.success("Distribution started successfully", distribution));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Mark distribution as delivered.
     */
    @PutMapping("/{id}/deliver")
    public ResponseEntity<ApiResponse<Distribution>> markDelivered(@PathVariable Long id) {
        try {
            Distribution distribution = distributionService.markDelivered(id);
            return ResponseEntity.ok(ApiResponse.success("Distribution marked as delivered successfully", distribution));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Cancel a distribution.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Distribution>> cancelDistribution(@PathVariable Long id) {
        try {
            Distribution distribution = distributionService.cancelDistribution(id);
            return ResponseEntity.ok(ApiResponse.success("Distribution cancelled successfully", distribution));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get distributions from a donor.
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<ApiResponse<List<Distribution>>> getDistributionsByDonor(@PathVariable Long donorId) {
        List<Distribution> distributions = distributionService.getDistributionsByDonorId(donorId);
        return ResponseEntity.ok(ApiResponse.success("Distributions from donor retrieved successfully", distributions));
    }

    /**
     * Get distributions to an NGO.
     */
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<ApiResponse<List<Distribution>>> getDistributionsByNgo(@PathVariable Long ngoId) {
        List<Distribution> distributions = distributionService.getDistributionsByNgoId(ngoId);
        return ResponseEntity.ok(ApiResponse.success("Distributions to NGO retrieved successfully", distributions));
    }

    /**
     * Get distributions by status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Distribution>>> getDistributionsByStatus(@PathVariable DistributionStatus status) {
        List<Distribution> distributions = distributionService.getDistributionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Distributions retrieved successfully", distributions));
    }

    /**
     * Get distributions by donation ID.
     */
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<ApiResponse<List<Distribution>>> getDistributionsByDonation(@PathVariable Long donationId) {
        List<Distribution> distributions = distributionService.getDistributionsByDonationId(donationId);
        return ResponseEntity.ok(ApiResponse.success("Distributions for donation retrieved successfully", distributions));
    }

    /**
     * Delete a distribution.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDistribution(@PathVariable Long id) {
        try {
            distributionService.deleteDistribution(id);
            return ResponseEntity.ok(ApiResponse.success("Distribution deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
