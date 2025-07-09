package com.womensfoundation.service;

import com.womensfoundation.model.ContactForm;
import com.womensfoundation.repository.ContactFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactFormService {

    @Autowired
    private ContactFormRepository repository;

    @Autowired
    private EmailService emailService;

    @SuppressWarnings("unused")
    public ContactForm saveContactForm(ContactForm form) {
        // Save form to the database
        ContactForm saved = repository.save(form);

        // Email content to admin
        String adminContent = "New Contact Form Submission:\n\n"
                + "Name: " + form.getName() + "\n"
                + "Email: " + form.getEmail() + "\n"
                + "Phone: " + form.getPhone() + "\n"
                + "Message:\n" + form.getMessage();

        // Send email to admin
        emailService.sendContactNotificationToAdmin(
                form.getName(), form.getEmail(), form.getPhone(), form.getMessage()
        );

        // Send confirmation email to the user
        emailService.sendConfirmationToUser(
                form.getEmail(),
                form.getName(),
                form.getPhone(),
                "Contact Form Submission"
        );

        return saved;
    }

    public List<ContactForm> getAllContactForms() {
        return repository.findAll();
    }

    public Optional<ContactForm> getContactFormById(Long id) {
        return repository.findById(id);
    }

    public List<ContactForm> getContactFormsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public long getContactFormsCountByStatus(String status) {
        return repository.countByStatus(status);
    }

    public ContactForm updateContactFormStatus(Long id, String status) {
        Optional<ContactForm> formOpt = repository.findById(id);
        if (formOpt.isPresent()) {
            ContactForm form = formOpt.get();
            form.setStatus(status);
            return repository.save(form);
        }
        throw new RuntimeException("Contact form not found");
    }

    public void deleteContactForm(Long id) {
        repository.deleteById(id);
    }
}
