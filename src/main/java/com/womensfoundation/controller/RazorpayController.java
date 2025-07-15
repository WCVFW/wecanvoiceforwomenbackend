package com.womensfoundation.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.womensfoundation.model.RazorpayKey;
import com.womensfoundation.repository.RazorpayKeyRepository;
import com.womensfoundation.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
@CrossOrigin(origins = "https://wecanvoiceforwomen.org")
public class RazorpayController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private RazorpayKeyRepository keyRepository;

    // ✅ Get Razorpay Public Key
    @GetMapping("/key")
    public ResponseEntity<?> getKey() {
        RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
        if (key == null || key.getRazorpayKeyId() == null) {
            return ResponseEntity.status(500).body(Map.of("error", "Razorpay Key not found"));
        }

        return ResponseEntity.ok(Map.of("key", key.getRazorpayKeyId()));
    }

    // ✅ Create Order
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> data) throws RazorpayException {
        int amount = data.get("amount");
        Order order = razorpayService.createRazorpayOrder(amount);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));

        return ResponseEntity.ok(response);
    }
}
