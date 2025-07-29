package com.womensfoundation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.womensfoundation.model.GetInvolved;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
                                name, email, phone, message);

                sendEmail(ADMIN_EMAIL, subject, content);
        }

        // 2. Confirm email to contact sender
        public void sendConfirmationToUser(String userEmail, String userName, String phone, String subjectFromForm) {
                String subject = "🤝 Thank you for contacting We Can Voice for Women!";
                String content = String.format(
                                "Dear %s,\n\n" +
                                                "Thank you for reaching out to us. We have received your message with the subject '%s' and will get back to you shortly.\n\n"
                                                +
                                                "Warm regards,\n" +
                                                "We Can Voice for Women Team",
                                userName, subjectFromForm != null ? subjectFromForm : "Contact Form");

                sendEmail(userEmail, subject, content);
        }

        // 3. Send donation receipt to donor
        public void sendDonationReceiptToDonor(String userEmail, String userName, String amount, String receiptId) {
                String subject = "🎉 Thank You for Your Donation!";

                String content = String.format(
                                "<!DOCTYPE html>" +
                                                "<html>" +
                                                "<head>" +
                                                "<style>" +
                                                "  body { font-family: Arial, sans-serif; color: #333; line-height: 1.6; }"
                                                +
                                                "  .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; }"
                                                +
                                                "  .header { background-color: #613EA3; color: #fff; padding: 10px 20px; border-radius: 8px 8px 0 0; }"
                                                +
                                                "  .footer { font-size: 0.9em; color: #777; margin-top: 30px; }" +
                                                "  .highlight { font-weight: bold; }" +
                                                "</style>" +
                                                "</head>" +
                                                "<body>" +
                                                "  <div class='container'>" +
                                                "    <div class='header'><h2>Thank You for Your Donation!</h2></div>" +
                                                "    <p>Dear <strong>%s</strong>,</p>" +
                                                "    <p>We sincerely thank you for your generous donation to <strong>We Can Voice for Women</strong>.</p>"
                                                +
                                                "    <h3>🧾 Donation Receipt:</h3>" +
                                                "    <p><span class='highlight'>Receipt ID:</span> %s<br>" +
                                                "    <span class='highlight'>Amount:</span> ₹%s</p>" +
                                                "    <p>Your support helps us continue our mission to empower women and girls in need.</p>"
                                                +
                                                "    <p>This donation is eligible for tax benefits under <strong>Section 80G</strong> of the Income Tax Act.</p>"
                                                +
                                                "    <p><span class='highlight'>80G Registration Number:</span> AAATW5359CF20251</p>"
                                                +
                                                "    <p>Warm regards,<br>" +
                                                "    <strong>We Can Voice for Women Foundation</strong></p>" +
                                                "    <div class='footer'>This is an automated receipt. Please keep it for your records.</div>"
                                                +
                                                "  </div>" +
                                                "</body>" +
                                                "</html>",
                                userName, receiptId, amount);

                sendEmail(userEmail, subject, content);
        }

        // 4. Notify admin about new donation
        public void sendDonationNotificationToAdmin(String donorName, String donorEmail, String amount,
                        String receiptId) {
                String subject = "💝 New Donation Received";
                String content = String.format(
                                "A new donation has been received:\n\n" +
                                                "Donor Name: %s\n" +
                                                "Email: %s\n" +
                                                "Amount: ₹%s\n" +
                                                "Receipt ID: %s\n\n" +
                                                "Please check the dashboard or database for further details.",
                                donorName, donorEmail, amount, receiptId);

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

        // 5. Notify admin about Get Involved form
        public void sendGetInvolvedFormToAdmin(GetInvolved data) {
    try {
        MimeMessage adminMessage = mailSender.createMimeMessage();
        MimeMessageHelper adminHelper = new MimeMessageHelper(adminMessage, true); // true = multipart

        adminHelper.setTo(ADMIN_EMAIL);
        adminHelper.setSubject("📥 New Get Involved Submission");

        StringBuilder body = new StringBuilder();
        body.append("New Get Involved Form Submission:\n\n")
            .append("Name: ").append(data.getFirstName()).append(" ")
            .append(data.getLastName()).append("\n")
            .append("Email: ").append(data.getEmail()).append("\n")
            .append("Phone: ").append(data.getPhone()).append("\n")
            .append("State: ").append(data.getState()).append("\n")
            .append("Aadhaar Number: ").append(data.getAadhaarNumber()).append("\n")
            .append("Interest: ").append(data.getInterest()).append("\n");

        if (data.getRoleAppliedFor() != null)
            body.append("Role Applied For: ").append(data.getRoleAppliedFor()).append("\n");
        if (data.getPartnerType() != null)
            body.append("Partner Type: ").append(data.getPartnerType()).append("\n");

        adminHelper.setText(body.toString());

        // Attach files if present
        if (data.getCvFile() != null) {
            adminHelper.addAttachment("CV_" + data.getFirstName() + ".pdf",
                new ByteArrayResource(data.getCvFile()));
        }

        if (data.getImageFile() != null) {
            adminHelper.addAttachment("Photo_" + data.getFirstName() + ".jpg",
                new ByteArrayResource(data.getImageFile()));
        }

        if (data.getAadhaarFile() != null) {
            adminHelper.addAttachment("Aadhaar_" + data.getFirstName() + ".pdf",
                new ByteArrayResource(data.getAadhaarFile()));
        }

        mailSender.send(adminMessage);

        // Now send confirmation to the user
        sendConfirmationEmailToUser(data);

    } catch (MessagingException e) {
        System.err.println("❌ Error sending Get Involved email: " + e.getMessage());
    }
}

private void sendConfirmationEmailToUser(GetInvolved data) {
    try {
        MimeMessage userMessage = mailSender.createMimeMessage();
        MimeMessageHelper userHelper = new MimeMessageHelper(userMessage, false); // no attachment

        userHelper.setTo(data.getEmail());
        userHelper.setSubject("✅ Thank you for getting involved!");

        String message = String.format(
            "Dear %s %s,\n\n"
            + "Thank you for your interest in getting involved with We Can Voice for Women Foundation. "
            + "We have received your submission and our team will review it shortly.\n\n"
            + "Here’s a quick summary:\n"
            + "Interest: %s\n"
            + (data.getRoleAppliedFor() != null ? "Role Applied For: " + data.getRoleAppliedFor() + "\n" : "")
            + (data.getPartnerType() != null ? "Partner Type: " + data.getPartnerType() + "\n" : "")
            + "\nIf you have any questions, feel free to reply to this email.\n\n"
            + "Warm regards,\nThe We Can Voice for Women Team",
            data.getFirstName(),
            data.getLastName(),
            data.getInterest()
        );

        userHelper.setText(message);

        mailSender.send(userMessage);

    } catch (MessagingException e) {
        System.err.println("❌ Error sending confirmation email to user: " + e.getMessage());
    }
}

}
