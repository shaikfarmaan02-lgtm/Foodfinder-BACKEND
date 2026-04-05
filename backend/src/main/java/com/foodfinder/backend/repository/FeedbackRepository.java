package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Feedback entity operations.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Find all feedback for a specific donation.
     * @param donationId the donation ID
     * @return list of feedback for that donation
     */
    List<Feedback> findByDonationId(Long donationId);

    /**
     * Find all feedback from a specific user.
     * @param userId the user ID
     * @return list of feedback from that user
     */
    List<Feedback> findByUserId(Long userId);

    /**
     * Find feedback by user and donation.
     * @param userId the user ID
     * @param donationId the donation ID
     * @return list of feedback
     */
    List<Feedback> findByUserIdAndDonationId(Long userId, Long donationId);
}
