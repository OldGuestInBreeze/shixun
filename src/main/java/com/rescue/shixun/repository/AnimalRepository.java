package com.rescue.shixun.repository;

import com.rescue.shixun.model.Animal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByStatusOrderByCreatedAtDesc(String status);

    List<Animal> findByNameContainingIgnoreCaseOrSpeciesContainingIgnoreCaseOrBreedContainingIgnoreCaseOrLocationContainingIgnoreCaseOrderByCreatedAtDesc(
            String name, String species, String breed, String location);
}
