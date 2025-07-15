package com.womensfoundation.repository;

import com.womensfoundation.model.RazorpayKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazorpayKeyRepository extends JpaRepository<RazorpayKey, Long> {
    RazorpayKey findTopByOrderByIdDesc();
}
