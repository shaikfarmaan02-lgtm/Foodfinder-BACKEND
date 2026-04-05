package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.ApprovalStatus;
import com.foodfinder.backend.model.Role;
import com.foodfinder.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email address.
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given email.
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find all users by role.
     * @param role the role to filter by (DONOR, NGO, ADMIN)
     * @return list of users with that role
     */
    List<User> findByRole(Role role);

    /**
     * Find all users by approval status.
     * @param approvalStatus the approval status
     * @return list of users with that approval status
     */
    List<User> findByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Find users by role and approval status.
     * @param role the role to filter by
     * @param approvalStatus the approval status
     * @return list of users matching both criteria
     */
    List<User> findByRoleAndApprovalStatus(Role role, ApprovalStatus approvalStatus);
}
