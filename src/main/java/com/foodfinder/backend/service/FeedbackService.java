package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.Feedback;
import com.foodfinder.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Feedback-related business logic.
 */
@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    /**
     * Create new feedback.
     * @param feedback the feedback to create
     * @return the saved feedback
     */
    public Feedback createFeedback(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    /**
     * Get all feedback.
     * @return list of all feedback
     */
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    /**
     * Get feedback by ID.
     * @param id the feedback ID
     * @return the feedback
     * @throws ResourceNotFoundException if feedback not found
     */
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }

    /**
     * Get feedback for a specific donation.
     * @param donationId the donation ID
     * @return list of feedback for that donation
     */
    public List<Feedback> getFeedbackByDonationId(Long donationId) {
        return feedbackRepository.findByDonationId(donationId);
    }

    /**
     * Get feedback from a specific user.
     * @param userId the user ID
     * @return list of feedback from that user
     */
    public List<Feedback> getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    /**
     * Get feedback for a user's specific donation.
     * @param userId the user ID
     * @param donationId the donation ID
     * @return list of feedback
     */
    public List<Feedback> getFeedbackByUserAndDonation(Long userId, Long donationId) {
        return feedbackRepository.findByUserIdAndDonationId(userId, donationId);
    }

    /**
     * Update feedback.
     * @param id the feedback ID
     * @param feedback the updated feedback data
     * @return the updated feedback
     */
    public Feedback updateFeedback(Long id, Feedback feedback) {
        Feedback existingFeedback = getFeedbackById(id);
        existingFeedback.setRating(feedback.getRating() != null ? feedback.getRating() : existingFeedback.getRating());
        existingFeedback.setComment(feedback.getComment() != null ? feedback.getComment() : existingFeedback.getComment());
        return feedbackRepository.save(existingFeedback);
    }

    /**
     * Delete feedback.
     * @param id the feedback ID to delete
     */
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
    }

    /**
     * Get average rating for a donation.
     * @param donationId the donation ID
     * @return average rating
     */
    public Double getAverageRatingForDonation(Long donationId) {
        List<Feedback> feedbackList = feedbackRepository.findByDonationId(donationId);
        if (feedbackList.isEmpty()) {
            return 0.0;
        }
        return feedbackList.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    /**
     * Count total number of feedback.
     * @return total feedback count
     */
    public long countFeedback() {
        return feedbackRepository.count();
    }
}
