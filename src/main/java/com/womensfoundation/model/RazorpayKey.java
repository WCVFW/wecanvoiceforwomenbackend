// === RazorpayKey.java ===
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

    // === Constructors ===
    public RazorpayKey() {
    }

    public RazorpayKey(Long id, String razorpayKeyId, String razorpayKeySecret) {
        this.id = id;
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
    }

    // === Getters ===
    public Long getId() {
        return id;
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public String getRazorpayKeySecret() {
        return razorpayKeySecret;
    }

    // === Setters ===
    public void setId(Long id) {
        this.id = id;
    }

    public void setRazorpayKeyId(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }

    public void setRazorpayKeySecret(String razorpayKeySecret) {
        this.razorpayKeySecret = razorpayKeySecret;
    }
}
