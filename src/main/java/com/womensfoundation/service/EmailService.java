package com.womensfoundation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 1. Email to the organization
    public void sendContactNotificationToAdmin(String name, String email, String phone, String message) {
        String subject = "New Contact Form Submission";
        String content = "You have received a new contact form submission:\n\n"
                + "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Phone: " + phone + "\n"
                + "Message:\n" + message;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("wecanvoiceforwomen@gmail.com"); // Change to your admin email
        mail.setSubject(subject);
        mail.setText(content);

        mailSender.send(mail);
    }

    // 2. Confirmation email to the user
    public void sendConfirmationToUser(String userEmail, String userName, String phone, String subjectFromForm) {
        String subject = "Thank you for contacting We Can Voice for Women!";

        String content = "Dear " + userName + ",\n\n"
                + "Thank you for reaching out to us. We have received your message and will get back to you shortly.\n\n"
                + "Here are the details you submitted:\n"
                // + "Name: " + userName + "\n"
                // + "Email: " + userEmail + "\n"
                // + "Phone: " + phone + "\n"
                // + "Subject: " + subjectFromForm + "\n\n"
                + "Warm regards,\n"
                + "We Can Voice for Women Team";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(userEmail);
        mail.setSubject(subject);
        mail.setText(content);
        mailSender.send(mail);
    }

}
