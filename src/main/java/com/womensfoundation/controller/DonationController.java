package com.womensfoundation.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.womensfoundation.model.Donation;
import com.womensfoundation.repository.DonationRepository;
import com.womensfoundation.service.EmailService;
import com.womensfoundation.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "https://wecanvoiceforwomen.org")
public class DonationController {

    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private EmailService emailService;

    // Create Razorpay Order
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> request) {
        Integer amount = request.get("amount");

        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid donation amount.");
        }

        try {
            Order order = razorpayService.createRazorpayOrder(amount);
            return ResponseEntity.ok(Map.of(
                    "id", order.get("id"),
                    "amount", order.get("amount"),
                    "currency", order.get("currency")
            ));
        } catch (RazorpayException e) {
            logger.error("❌ Failed to create Razorpay order", e);
            return ResponseEntity.internalServerError().body("Failed to create Razorpay order.");
        }
    }

    // Verify Payment & Save Donation
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Donation donation) {
        logger.info("🔍 Received donation verification request: {}", donation);

        // Validate Razorpay fields
        if (donation.getRazorpayOrderId() == null ||
            donation.getRazorpayPaymentId() == null ||
            donation.getRazorpaySignature() == null) {
            return ResponseEntity.badRequest().body("Missing Razorpay signature data.");
        }

        try {
            boolean isValid = razorpayService.verifySignature(
                    donation.getRazorpayOrderId(),
                    donation.getRazorpayPaymentId(),
                    donation.getRazorpaySignature());

            if (!isValid) {
                logger.warn("❌ Invalid Razorpay signature for order: {}", donation.getRazorpayOrderId());
                return ResponseEntity.badRequest().body("Invalid payment signature.");
            }

            // Validate essential donor info
            if (donation.getName() == null || donation.getEmail() == null || donation.getAmount() == null) {
                return ResponseEntity.badRequest().body("Missing donor information.");
            }

            // Generate receipt & save
            donation.setCreatedAt(LocalDateTime.now());
            String receiptId = "RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            donation.setReceiptId(receiptId);

            Donation savedDonation = donationRepository.save(donation);
            logger.info("✅ Donation saved: ID={}, Name={}, Amount={}", receiptId, savedDonation.getName(), savedDonation.getAmount());

            // Send emails
            try {
                emailService.sendDonationReceiptToDonor(
                        savedDonation.getEmail(),
                        savedDonation.getName(),
                        savedDonation.getAmount(),
                        receiptId
                );
                emailService.sendDonationNotificationToAdmin(
                        savedDonation.getName(),
                        savedDonation.getEmail(),
                        savedDonation.getAmount(),
                        receiptId
                );
            } catch (Exception e) {
                logger.error("❌ Error sending emails for donation {}", receiptId, e);
            }

            // Send back full donation info
            return ResponseEntity.ok(Map.of(
                    "receiptId", receiptId,
                    "name", savedDonation.getName(),
                    "email", savedDonation.getEmail(),
                    "amount", savedDonation.getAmount(),
                    "donationType", savedDonation.getDonationType()
            ));

        } catch (SignatureException e) {
            logger.error("❌ SignatureException during verification", e);
            return ResponseEntity.internalServerError().body("Error verifying signature.");
        } catch (Exception e) {
            logger.error("❌ Unknown error verifying donation", e);
            return ResponseEntity.internalServerError().body("Something went wrong. Please try again.");
        }
    }
}
