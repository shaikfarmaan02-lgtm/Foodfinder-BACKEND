package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.model.Feedback;
import com.foodfinder.backend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Feedback operations.
 * 
 * Endpoints:
 * - POST /api/feedback - Create new feedback
 * - GET /api/feedback - Get all feedback
 * - GET /api/feedback/{id} - Get feedback by ID
 * - PUT /api/feedback/{id} - Update feedback
 * - DELETE /api/feedback/{id} - Delete feedback
 * - GET /api/feedback/donation/{donationId} - Get feedback for donation
 * - GET /api/feedback/user/{userId} - Get feedback from user
 * - GET /api/feedback/donation/{donationId}/average - Get average rating for donation
 */
@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * Create new feedback.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Feedback>> createFeedback(
            @Valid @RequestBody Feedback feedback) {
        try {
            Feedback createdFeedback = feedbackService.createFeedback(feedback);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Feedback created successfully", createdFeedback));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all feedback.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Feedback>>> getAllFeedback() {
        List<Feedback> feedback = feedbackService.getAllFeedback();
        return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", feedback));
    }

    /**
     * Get feedback by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Feedback>> getFeedbackById(@PathVariable Long id) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(ApiResponse.success("Feedback retrieved successfully", feedback));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update feedback.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Feedback>> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody Feedback feedback) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
            return ResponseEntity.ok(ApiResponse.success("Feedback updated successfully", updatedFeedback));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete feedback.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(ApiResponse.success("Feedback deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get feedback for a specific donation.
     */
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbackByDonation(@PathVariable Long donationId) {
        List<Feedback> feedback = feedbackService.getFeedbackByDonationId(donationId);
        return ResponseEntity.ok(ApiResponse.success("Feedback for donation retrieved successfully", feedback));
    }

    /**
     * Get feedback from a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbackByUser(@PathVariable Long userId) {
        List<Feedback> feedback = feedbackService.getFeedbackByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Feedback from user retrieved successfully", feedback));
    }

    /**
     * Get average rating for a donation.
     */
    @GetMapping("/donation/{donationId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Long donationId) {
        Double averageRating = feedbackService.getAverageRatingForDonation(donationId);
        return ResponseEntity.ok(ApiResponse.success("Average rating retrieved successfully", averageRating));
    }

    /**
     * Get feedback count.
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getFeedbackCount() {
        long count = feedbackService.countFeedback();
        return ResponseEntity.ok(ApiResponse.success("Feedback count retrieved successfully", count));
    }
}
