package com.womensfoundation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String ADMIN_EMAIL = "wecanvoiceforwomen@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    // 1. Notify admin of contact form
    public void sendContactNotificationToAdmin(String name, String email, String phone, String message) {
        String subject = "📩 New Contact Form Submission";
        String content = String.format(
                "You have received a new contact form submission:\n\n" +
                "Name: %s\n" +
                "Email: %s\n" +
                "Phone: %s\n" +
                "Message:\n%s",
                name, email, phone, message
        );

        sendEmail(ADMIN_EMAIL, subject, content);
    }

    // 2. Confirm email to contact sender
    public void sendConfirmationToUser(String userEmail, String userName, String phone, String subjectFromForm) {
        String subject = "🤝 Thank you for contacting We Can Voice for Women!";
        String content = String.format(
                "Dear %s,\n\n" +
                "Thank you for reaching out to us. We have received your message with the subject '%s' and will get back to you shortly.\n\n" +
                "Warm regards,\n" +
                "We Can Voice for Women Team",
                userName, subjectFromForm != null ? subjectFromForm : "Contact Form"
        );

        sendEmail(userEmail, subject, content);
    }

    // 3. Send donation receipt to donor
    public void sendDonationReceiptToDonor(String userEmail, String userName, String amount, String receiptId) {
        String subject = "🎉 Thank You for Your Donation!";
        String content = String.format(
                "Dear %s,\n\n" +
                "We sincerely thank you for your generous donation to We Can Voice for Women.\n\n" +
                "🧾 Donation Receipt:\n" +
                "Receipt ID: %s\n" +
                "Amount: ₹%s\n\n" +
                "Your support helps us continue our mission to empower women and girls in need.\n" +
                "This donation is eligible for tax benefits under Section 80G of the Income Tax Act.\n\n" +
                "Warm regards,\n" +
                "We Can Voice for Women Foundation",
                userName, receiptId, amount
        );

        sendEmail(userEmail, subject, content);
    }

    // 4. Notify admin about new donation
    public void sendDonationNotificationToAdmin(String donorName, String donorEmail, String amount, String receiptId) {
        String subject = "💝 New Donation Received";
        String content = String.format(
                "A new donation has been received:\n\n" +
                "Donor Name: %s\n" +
                "Email: %s\n" +
                "Amount: ₹%s\n" +
                "Receipt ID: %s\n\n" +
                "Please check the dashboard or database for further details.",
                donorName, donorEmail, amount, receiptId
        );

        sendEmail(ADMIN_EMAIL, subject, content);
    }

    // ✅ Common private method to send emails
    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(content);
            mailSender.send(mail);
        } catch (Exception e) {
            // Optional: log error or send to fallback email
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
