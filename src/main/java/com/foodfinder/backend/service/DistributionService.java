package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.Distribution;
import com.foodfinder.backend.model.DistributionStatus;
import com.foodfinder.backend.repository.DistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Distribution-related business logic.
 */
@Service
public class DistributionService {

    @Autowired
    private DistributionRepository distributionRepository;

    /**
     * Create a new distribution.
     * @param distribution the distribution to create
     * @return the saved distribution
     */
    public Distribution createDistribution(Distribution distribution) {
        distribution.setDistributionDate(LocalDateTime.now());
        distribution.setStatus(DistributionStatus.PENDING);
        return distributionRepository.save(distribution);
    }

    /**
     * Get all distributions.
     * @return list of all distributions
     */
    public List<Distribution> getAllDistributions() {
        return distributionRepository.findAll();
    }

    /**
     * Get distribution by ID.
     * @param id the distribution ID
     * @return the distribution
     * @throws ResourceNotFoundException if distribution not found
     */
    public Distribution getDistributionById(Long id) {
        return distributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribution not found with id: " + id));
    }

    /**
     * Get distributions for a specific donation.
     * @param donationId the donation ID
     * @return list of distributions for that donation
     */
    public List<Distribution> getDistributionsByDonationId(Long donationId) {
        return distributionRepository.findByDonationId(donationId);
    }

    /**
     * Get distributions from a specific donor.
     * @param donorId the donor ID
     * @return list of distributions from that donor
     */
    public List<Distribution> getDistributionsByDonorId(Long donorId) {
        return distributionRepository.findByDonorId(donorId);
    }

    /**
     * Get distributions to a specific NGO.
     * @param ngoId the NGO ID
     * @return list of distributions to that NGO
     */
    public List<Distribution> getDistributionsByNgoId(Long ngoId) {
        return distributionRepository.findByNgoId(ngoId);
    }

    /**
     * Get distributions by status.
     * @param status the distribution status
     * @return list of distributions with that status
     */
    public List<Distribution> getDistributionsByStatus(DistributionStatus status) {
        return distributionRepository.findByStatus(status);
    }

    /**
     * Update distribution status to IN_TRANSIT.
     * @param id the distribution ID
     * @return the updated distribution
     */
    public Distribution startDistribution(Long id) {
        Distribution distribution = getDistributionById(id);
        distribution.setStatus(DistributionStatus.IN_TRANSIT);
        return distributionRepository.save(distribution);
    }

    /**
     * Mark distribution as delivered.
     * @param id the distribution ID
     * @return the updated distribution
     */
    public Distribution markDelivered(Long id) {
        Distribution distribution = getDistributionById(id);
        distribution.setStatus(DistributionStatus.DELIVERED);
        distribution.setDeliveredDate(LocalDateTime.now());
        return distributionRepository.save(distribution);
    }

    /**
     * Cancel a distribution.
     * @param id the distribution ID
     * @return the updated distribution
     */
    public Distribution cancelDistribution(Long id) {
        Distribution distribution = getDistributionById(id);
        distribution.setStatus(DistributionStatus.CANCELLED);
        return distributionRepository.save(distribution);
    }

    /**
     * Delete a distribution.
     * @param id the distribution ID to delete
     */
    public void deleteDistribution(Long id) {
        if (!distributionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Distribution not found with id: " + id);
        }
        distributionRepository.deleteById(id);
    }

    /**
     * Count total number of distributions.
     * @return total distribution count
     */
    public long countDistributions() {
        return distributionRepository.count();
    }
}
