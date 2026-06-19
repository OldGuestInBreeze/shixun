package com.rescue.shixun.repository;

import com.rescue.shixun.model.AdoptionApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    long countByStatus(String status);

    List<AdoptionApplication> findAllByOrderByCreatedAtDesc();
}
