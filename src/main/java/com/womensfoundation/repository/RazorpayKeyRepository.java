package com.womensfoundation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.womensfoundation.model.RazorpayKey;

public interface RazorpayKeyRepository extends JpaRepository<RazorpayKey, Long> {
    RazorpayKey findTopByOrderByIdDesc(); // Gets the latest added key
}
