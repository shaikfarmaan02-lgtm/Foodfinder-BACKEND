package com.foodfinder.backend.controller;

import com.foodfinder.backend.dto.ApiResponse;
import com.foodfinder.backend.model.Contact;
import com.foodfinder.backend.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Contact/Inquiry operations.
 * 
 * Endpoints:
 * - POST /api/contact - Create new contact message
 * - GET /api/contact - Get all contact messages (ADMIN only)
 * - GET /api/contact/{id} - Get contact by ID (ADMIN only)
 * - GET /api/contact/unresolved - Get unresolved messages (ADMIN only)
 * - PUT /api/contact/{id}/resolve - Mark as resolved (ADMIN only)
 * - DELETE /api/contact/{id} - Delete contact (ADMIN only)
 * - GET /api/contact/email/{email} - Get contacts by email
 * - GET /api/contact/search - Search by subject
 */
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * Create new contact message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Contact>> createContact(
            @Valid @RequestBody Contact contact) {
        try {
            Contact createdContact = contactService.createContact(contact);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Contact message sent successfully. We will get back to you soon.", createdContact));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all contact messages (ADMIN only).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Contact>>> getAllContacts(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(ApiResponse.success("Contact messages retrieved successfully", contacts));
    }

    /**
     * Get contact message by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Contact>> getContactById(@PathVariable Long id) {
        try {
            Contact contact = contactService.getContactById(id);
            return ResponseEntity.ok(ApiResponse.success("Contact message retrieved successfully", contact));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get unresolved contact messages (ADMIN only).
     */
    @GetMapping("/unresolved")
    public ResponseEntity<ApiResponse<List<Contact>>> getUnresolvedContacts(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        List<Contact> contacts = contactService.getUnresolvedContacts();
        return ResponseEntity.ok(ApiResponse.success("Unresolved contact messages retrieved successfully", contacts));
    }

    /**
     * Mark contact message as resolved (ADMIN only).
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<Contact>> resolveContact(
            @PathVariable Long id,
            @RequestParam String adminNotes,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            Contact contact = contactService.resolveContact(id, adminNotes);
            return ResponseEntity.ok(ApiResponse.success("Contact message marked as resolved successfully", contact));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete contact message (ADMIN only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.ok(ApiResponse.success("Contact message deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get contacts by email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<List<Contact>>> getContactsByEmail(@PathVariable String email) {
        List<Contact> contacts = contactService.getContactsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Contact messages retrieved successfully", contacts));
    }

    /**
     * Search contacts by subject.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Contact>>> searchBySubject(@RequestParam String subject) {
        List<Contact> contacts = contactService.searchBySubject(subject);
        return ResponseEntity.ok(ApiResponse.success("Contact messages retrieved successfully", contacts));
    }

    /**
     * Get contact count.
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getContactCount(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        long count = contactService.countContacts();
        return ResponseEntity.ok(ApiResponse.success("Contact message count retrieved successfully", count));
    }

    /**
     * Get unresolved contact count.
     */
    @GetMapping("/count-unresolved")
    public ResponseEntity<ApiResponse<Long>> getUnresolvedCount(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        long count = contactService.countUnresolved();
        return ResponseEntity.ok(ApiResponse.success("Unresolved contact message count retrieved successfully", count));
    }
}
