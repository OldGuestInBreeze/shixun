package com.rescue.shixun.repository;

import com.rescue.shixun.model.Volunteer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findAllByOrderByCreatedAtDesc();
}
