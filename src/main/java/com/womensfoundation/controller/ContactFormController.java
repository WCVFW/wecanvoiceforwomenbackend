package com.womensfoundation.controller;

import com.womensfoundation.model.ContactForm;
import com.womensfoundation.service.ContactFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = {
    "http://wecanvoiceforwomen.org:8080",
    "http://localhost:5173" // optional for local testing
})
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    // Submit Contact Form
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitContactForm(@Valid @RequestBody ContactForm contactForm) {
        ContactForm saved = contactFormService.saveContactForm(contactForm);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Contact form submitted successfully");
        response.put("id", saved.getId());
        return ResponseEntity.ok(response);
    }

    // Get All Contact Forms
    @GetMapping
    public ResponseEntity<List<ContactForm>> getAllContactForms() {
        return ResponseEntity.ok(contactFormService.getAllContactForms());
    }

    // Get Contact Form by ID
    @GetMapping("/{id}")
    public ResponseEntity<ContactForm> getContactFormById(@PathVariable Long id) {
        return contactFormService.getContactFormById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Contact Forms by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContactForm>> getContactFormsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(contactFormService.getContactFormsByStatus(status));
    }

    // Update Contact Form Status
    @PutMapping("/{id}/status")
    public ResponseEntity<ContactForm> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        return ResponseEntity.ok(contactFormService.updateContactFormStatus(id, status));
    }

    // Delete Contact Form
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteForm(@PathVariable Long id) {
        contactFormService.deleteContactForm(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Get Contact Form Statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", contactFormService.getAllContactForms().size());
        stats.put("new", contactFormService.getContactFormsCountByStatus("NEW"));
        stats.put("inProgress", contactFormService.getContactFormsCountByStatus("IN_PROGRESS"));
        stats.put("resolved", contactFormService.getContactFormsCountByStatus("RESOLVED"));
        return ResponseEntity.ok(stats);
    }
}
