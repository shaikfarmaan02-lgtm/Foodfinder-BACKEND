package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.dto.LoginRequest;
import com.foodfinder.backend.dto.LoginResponse;
import com.foodfinder.backend.dto.RegisterRequest;
import com.foodfinder.backend.model.ApprovalStatus;
import com.foodfinder.backend.model.Role;
import com.foodfinder.backend.model.User;
import com.foodfinder.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for User-related business logic.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user.
     * @param registerRequest the registration request
     * @return the saved user
     * @throws RuntimeException if email already exists
     */
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already registered: " + registerRequest.getEmail());
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setOrganization(registerRequest.getOrganization());
        user.setCity(registerRequest.getCity());
        user.setState(registerRequest.getState());
        user.setPincode(registerRequest.getPincode());

        // Auto approve ADMIN, others start as PENDING
        if (registerRequest.getRole() == Role.ADMIN) {
            user.setApprovalStatus(ApprovalStatus.APPROVED);
        } else {
            user.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
        }

        return userRepository.save(user);
    }

    /**
     * Login user.
     * @param loginRequest the login request
     * @return LoginResponse with user details and access status
     * @throws RuntimeException if credentials are invalid
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setApprovalStatus(user.getApprovalStatus());

        // Check approval status
        if (user.getRole() == Role.ADMIN) {
            if (user.getApprovalStatus() == ApprovalStatus.APPROVED) {
                response.setCanAccess(true);
                response.setMessage("Login successful");
            } else {
                response.setCanAccess(false);
                response.setMessage("Admin account not approved");
            }
        } else if (user.getRole() == Role.DONOR || user.getRole() == Role.NGO) {
            if (user.getApprovalStatus() == ApprovalStatus.APPROVED) {
                response.setCanAccess(true);
                response.setMessage("Login successful");
            } else if (user.getApprovalStatus() == ApprovalStatus.PENDING_APPROVAL) {
                response.setCanAccess(false);
                response.setMessage("Waiting for admin approval");
            } else if (user.getApprovalStatus() == ApprovalStatus.REJECTED) {
                response.setCanAccess(false);
                response.setMessage("Account rejected");
            } else if (user.getApprovalStatus() == ApprovalStatus.SUSPENDED) {
                response.setCanAccess(false);
                response.setMessage("Account suspended");
            }
        }

        return response;
    }

    /**
     * Get all users.
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID.
     * @param id the user ID
     * @return the user
     * @throws ResourceNotFoundException if user not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Get users by role.
     * @param role the role to filter by
     * @return list of users with that role
     */
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * Get users by approval status.
     * @param approvalStatus the approval status
     * @return list of users with that status
     */
    public List<User> getUsersByApprovalStatus(ApprovalStatus approvalStatus) {
        return userRepository.findByApprovalStatus(approvalStatus);
    }

    /**
     * Approve a user account (ADMIN only).
     * @param userId the user ID to approve
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     */
    public User approveUser(Long userId) {
        User user = getUserById(userId);
        user.setApprovalStatus(ApprovalStatus.APPROVED);
        return userRepository.save(user);
    }

    /**
     * Reject a user account (ADMIN only).
     * @param userId the user ID to reject
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     */
    public User rejectUser(Long userId) {
        User user = getUserById(userId);
        user.setApprovalStatus(ApprovalStatus.REJECTED);
        return userRepository.save(user);
    }

    /**
     * Suspend a user account (ADMIN only).
     * @param userId the user ID to suspend
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     */
    public User suspendUser(Long userId) {
        User user = getUserById(userId);
        user.setApprovalStatus(ApprovalStatus.SUSPENDED);
        return userRepository.save(user);
    }

    /**
     * Update user profile.
     * @param userId the user ID to update
     * @param user the updated user data
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     */
    public User updateUser(Long userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setPhone(user.getPhone() != null ? user.getPhone() : existingUser.getPhone());
        existingUser.setAddress(user.getAddress() != null ? user.getAddress() : existingUser.getAddress());
        existingUser.setOrganization(user.getOrganization() != null ? user.getOrganization() : existingUser.getOrganization());
        existingUser.setCity(user.getCity() != null ? user.getCity() : existingUser.getCity());
        existingUser.setState(user.getState() != null ? user.getState() : existingUser.getState());
        existingUser.setPincode(user.getPincode() != null ? user.getPincode() : existingUser.getPincode());
        return userRepository.save(existingUser);
    }

    /**
     * Delete user by ID.
     * @param id the user ID to delete
     * @throws ResourceNotFoundException if user not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Count total number of users.
     * @return total user count
     */
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * Validate if user can perform DONOR actions.
     * @param userId the user ID
     * @return true if user is an approved donor
     */
    public boolean validateDonor(Long userId) {
        User user = getUserById(userId);
        return user.getRole() == Role.DONOR && user.getApprovalStatus() == ApprovalStatus.APPROVED;
    }

    /**
     * Validate if user can perform NGO actions.
     * @param userId the user ID
     * @return true if user is an approved NGO
     */
    public boolean validateNGO(Long userId) {
        User user = getUserById(userId);
        return user.getRole() == Role.NGO && user.getApprovalStatus() == ApprovalStatus.APPROVED;
    }

    /**
     * Validate if user is an admin.
     * @param userId the user ID
     * @return true if user is an admin
     */
    public boolean validateAdmin(Long userId) {
        User user = getUserById(userId);
        return user.getRole() == Role.ADMIN;
    }
}
