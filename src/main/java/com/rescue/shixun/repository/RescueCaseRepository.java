package com.rescue.shixun.repository;

import com.rescue.shixun.model.RescueCase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RescueCaseRepository extends JpaRepository<RescueCase, Long> {
    List<RescueCase> findAllByOrderByReportedAtDesc();
}
