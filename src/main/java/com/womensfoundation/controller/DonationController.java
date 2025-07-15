// === DonationController.java ===
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

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> request) throws RazorpayException {
        int amount = request.get("amount");
        Order order = razorpayService.createRazorpayOrder(amount);

        return ResponseEntity.ok(Map.of(
                "id", order.get("id"),
                "amount", order.get("amount"),
                "currency", order.get("currency")
        ));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Donation donation) throws SignatureException {
        System.out.println("🔍 Received donation: " + donation);

        if (donation.getRazorpayOrderId() == null ||
                donation.getRazorpayPaymentId() == null ||
                donation.getRazorpaySignature() == null) {
            return ResponseEntity.badRequest().body("Missing Razorpay signature data");
        }

        boolean isValid = razorpayService.verifySignature(
                donation.getRazorpayOrderId(),
                donation.getRazorpayPaymentId(),
                donation.getRazorpaySignature());

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }

        donation.setCreatedAt(LocalDateTime.now());
        String receiptId = "RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        donation.setReceiptId(receiptId);

        Donation savedDonation = donationRepository.save(donation);

        try {
            emailService.sendDonationReceiptToDonor(savedDonation.getEmail(), savedDonation.getName(),
                    savedDonation.getAmount(), receiptId);
            emailService.sendDonationNotificationToAdmin(savedDonation.getName(), savedDonation.getEmail(),
                    savedDonation.getAmount(), receiptId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Map.of("receiptId", receiptId));
    }
}
