package com.womensfoundation.repository;

import com.womensfoundation.model.RazorpayKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RazorpayKeyRepository extends JpaRepository<RazorpayKey, Long> {
    RazorpayKey findTopByOrderByIdDesc();
}
