package com.womensfoundation.repository;

import com.womensfoundation.model.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ContactFormRepository extends JpaRepository<ContactForm, Long> {
    List<ContactForm> findByStatus(String status);

    @Query("SELECT c FROM ContactForm c WHERE c.createdAt BETWEEN ?1 AND ?2")
    List<ContactForm> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT c FROM ContactForm c ORDER BY c.createdAt DESC")
    List<ContactForm> findAllOrderByCreatedAtDesc();

    long countByStatus(String status);
}
