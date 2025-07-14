package com.womensfoundation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.womensfoundation.model.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}