package cc.jcguzman.petadoptionapi.repository;

import cc.jcguzman.petadoptionapi.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByKeyValueAndActiveTrue(String keyValue);
    boolean existsByKeyValue(String keyValue);
}