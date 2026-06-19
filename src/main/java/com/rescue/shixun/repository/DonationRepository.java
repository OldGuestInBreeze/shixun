package com.rescue.shixun.repository;

import com.rescue.shixun.model.Donation;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findAllByOrderByCreatedAtDesc();

    @Query("select coalesce(sum(d.amount), 0) from Donation d")
    BigDecimal totalAmount();
}
