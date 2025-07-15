package com.womensfoundation.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.womensfoundation.model.RazorpayKey;
import com.womensfoundation.repository.RazorpayKeyRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;

@Service
public class RazorpayService {

    private RazorpayClient client;

    @Autowired
    private RazorpayKeyRepository keyRepository;

    @PostConstruct
    public void init() throws RazorpayException {
        RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
        if (key == null || key.getRazorpayKeyId() == null || key.getRazorpayKeySecret() == null) {
            throw new RazorpayException("Razorpay key or secret not found in database");
        }
        this.client = new RazorpayClient(key.getRazorpayKeyId(), key.getRazorpayKeySecret());
    }

    public Order createRazorpayOrder(int amount) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", amount * 100);  // amount in paise
        options.put("currency", "INR");
        options.put("receipt", "receipt_" + System.currentTimeMillis());
        options.put("payment_capture", 1);
        return client.orders.create(options);
    }

    public boolean verifySignature(String orderId, String paymentId, String signature) throws SignatureException {
        try {
            RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
            if (key == null || key.getRazorpayKeySecret() == null) {
                throw new SignatureException("Razorpay secret key missing");
            }

            String payload = orderId + "|" + paymentId;
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getRazorpayKeySecret().getBytes(), "HmacSHA256");
            sha256.init(secretKey);
            byte[] hash = sha256.doFinal(payload.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString().equals(signature);
        } catch (Exception e) {
            throw new SignatureException("Unable to verify Razorpay signature", e);
        }
    }
}
