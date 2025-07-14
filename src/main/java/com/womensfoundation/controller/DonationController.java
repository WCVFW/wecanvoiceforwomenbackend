package com.womensfoundation.controller;

import com.razorpay.Order;
import com.womensfoundation.model.Donation;
import com.womensfoundation.model.RazorpayKey;
import com.womensfoundation.service.DonationService;
import com.womensfoundation.repository.RazorpayKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = { "http://localhost:5173", "http://13.60.235.97" })
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private RazorpayKeyRepository keyRepository;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> data) throws Exception {
        Order order = donationService.createOrder(data.get("amount"));
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Donation donation) throws Exception {
        RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
        boolean isValid = donationService.verifySignature(
                donation.getRazorpayOrderId(),
                donation.getRazorpayOrderId(),
                donation.getRazorpaySignature(),
                key.getKeySecret()
        );
        if (isValid) {
            Donation saved = donationService.saveDonation(donation);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("donationId", saved.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }
    }

    @GetMapping("/key")
    public ResponseEntity<?> getRazorpayKey() {
        RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
        Map<String, String> map = new HashMap<>();
        map.put("keyId", key.getKeyId());
        return ResponseEntity.ok(map);
    }
}
