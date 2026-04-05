package com.foodfinder.backend.service;

import com.foodfinder.backend.config.ResourceNotFoundException;
import com.foodfinder.backend.model.Contact;
import com.foodfinder.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Contact-related business logic.
 */
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    /**
     * Create new contact message.
     * @param contact the contact message to create
     * @return the saved contact message
     */
    public Contact createContact(Contact contact) {
        contact.setCreatedAt(LocalDateTime.now());
        contact.setResolved(false);
        return contactRepository.save(contact);
    }

    /**
     * Get all contact messages.
     * @return list of all contact messages
     */
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    /**
     * Get contact message by ID.
     * @param id the contact ID
     * @return the contact message
     * @throws ResourceNotFoundException if contact not found
     */
    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));
    }

    /**
     * Get contact messages by email.
     * @param email the email address
     * @return list of contact messages from that email
     */
    public List<Contact> getContactsByEmail(String email) {
        return contactRepository.findByEmail(email);
    }

    /**
     * Get unresolved contact messages.
     * @return list of unresolved contact messages
     */
    public List<Contact> getUnresolvedContacts() {
        return contactRepository.findByResolved(false);
    }

    /**
     * Get resolved contact messages.
     * @return list of resolved contact messages
     */
    public List<Contact> getResolvedContacts() {
        return contactRepository.findByResolved(true);
    }

    /**
     * Search contact messages by subject.
     * @param subject the subject to search for
     * @return list of contact messages matching the subject
     */
    public List<Contact> searchBySubject(String subject) {
        return contactRepository.findBySubjectContainingIgnoreCase(subject);
    }

    /**
     * Mark contact message as resolved.
     * @param id the contact ID
     * @param adminNotes notes from admin
     * @return the updated contact message
     */
    public Contact resolveContact(Long id, String adminNotes) {
        Contact contact = getContactById(id);
        contact.setResolved(true);
        contact.setAdminNotes(adminNotes);
        return contactRepository.save(contact);
    }

    /**
     * Update contact message.
     * @param id the contact ID
     * @param contact the updated contact data
     * @return the updated contact message
     */
    public Contact updateContact(Long id, Contact contact) {
        Contact existingContact = getContactById(id);
        existingContact.setAdminNotes(contact.getAdminNotes() != null ? contact.getAdminNotes() : existingContact.getAdminNotes());
        existingContact.setResolved(contact.getResolved() != null ? contact.getResolved() : existingContact.getResolved());
        return contactRepository.save(existingContact);
    }

    /**
     * Delete contact message.
     * @param id the contact ID to delete
     */
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact message not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }

    /**
     * Count total number of contact messages.
     * @return total contact count
     */
    public long countContacts() {
        return contactRepository.count();
    }

    /**
     * Count unresolved contact messages.
     * @return count of unresolved messages
     */
    public long countUnresolved() {
        return getUnresolvedContacts().size();
    }
}
