package com.womensfoundation.service;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.womensfoundation.model.Donation;
import com.womensfoundation.model.RazorpayKey;
import com.womensfoundation.repository.DonationRepository;
import com.womensfoundation.repository.RazorpayKeyRepository;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private RazorpayKeyRepository keyRepository;

    public Order createOrder(String amount) throws Exception {
        RazorpayKey key = keyRepository.findTopByOrderByIdDesc();
        RazorpayClient client = new RazorpayClient(key.getKeyId(), key.getKeySecret());

        JSONObject options = new JSONObject();
        options.put("amount", Integer.parseInt(amount) * 100);
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        return client.orders.create(options);
    }

    public boolean verifySignature(String orderId, String paymentId, String signature, String keySecret) throws Exception {
        String data = orderId + "|" + paymentId;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(keySecret.getBytes(), "HmacSHA256"));
        String hash = Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        return hash.equals(signature);
    }

    public Donation saveDonation(Donation donation) {
        return donationRepository.save(donation);
    }
}