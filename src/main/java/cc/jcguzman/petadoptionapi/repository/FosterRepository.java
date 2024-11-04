package cc.jcguzman.petadoptionapi.repository;

import cc.jcguzman.petadoptionapi.model.Foster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FosterRepository extends JpaRepository<Foster, UUID> {
    List<Foster> findByActiveTrue();

    @Query("SELECT f FROM Foster f WHERE f.active = true AND SIZE(f.petsAssigned) < f.maxPets")
    List<Foster> findAvailableFosters();

    boolean existsByEmail(String email);
}