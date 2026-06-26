package com.rescue.shixun.repository;

import com.rescue.shixun.model.AdoptionApplication;
import com.rescue.shixun.model.Animal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    long countByStatus(String status);

    long countByAnimalAndStatus(Animal animal, String status);

    @EntityGraph(attributePaths = "animal")
    Optional<AdoptionApplication> findById(Long id);

    @EntityGraph(attributePaths = "animal")
    List<AdoptionApplication> findAllByOrderByCreatedAtDesc();
}
