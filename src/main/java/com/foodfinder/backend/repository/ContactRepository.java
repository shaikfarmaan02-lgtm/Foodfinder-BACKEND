package com.foodfinder.backend.repository;

import com.foodfinder.backend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Contact entity operations.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * Find all contact messages by email.
     * @param email the email address
     * @return list of contact messages from that email
     */
    List<Contact> findByEmail(String email);

    /**
     * Find all unresolved contact messages.
     * @param resolved the resolved status
     * @return list of unresolved contact messages
     */
    List<Contact> findByResolved(Boolean resolved);

    /**
     * Find all contact messages by subject.
     * @param subject the subject
     * @return list of contact messages with that subject
     */
    List<Contact> findBySubjectContainingIgnoreCase(String subject);
}
