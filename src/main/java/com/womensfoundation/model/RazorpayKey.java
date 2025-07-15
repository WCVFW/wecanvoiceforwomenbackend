package com.womensfoundation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "razorpay_key")
public class RazorpayKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_id")
    private String razorpayKeyId;

    @Column(name = "key_secret")
    private String razorpayKeySecret;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRazorpayKeyId() { return razorpayKeyId; }
    public void setRazorpayKeyId(String razorpayKeyId) { this.razorpayKeyId = razorpayKeyId; }

    public String getRazorpayKeySecret() { return razorpayKeySecret; }
    public void setRazorpayKeySecret(String razorpayKeySecret) { this.razorpayKeySecret = razorpayKeySecret; }
}
