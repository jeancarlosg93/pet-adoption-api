package cc.jcguzman.petadoptionapi.service;

import cc.jcguzman.petadoptionapi.model.ApiKey;
import cc.jcguzman.petadoptionapi.repository.ApiKeyRepository;
import cc.jcguzman.petadoptionapi.util.ApiKeyGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Transactional
    public ApiKey generateKey(String description, String createdBy, Integer validityDays) {
        ApiKey apiKey = new ApiKey();
        apiKey.setKeyValue(ApiKeyGenerator.generatePrefixedKey("pat"));
        apiKey.setDescription(description);
        apiKey.setCreatedBy(createdBy);

        if (validityDays != null) {
            apiKey.setExpiresAt(Instant.now().plus(validityDays, ChronoUnit.DAYS));
        }

        return apiKeyRepository.save(apiKey);
    }

    @Transactional(readOnly = true)
    public boolean validateKey(String keyValue) {
        return apiKeyRepository.findByKeyValueAndActiveTrue(keyValue)
                .map(ApiKey::isValid)
                .orElse(false);
    }

    @Transactional
    public void revokeKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("API key not found"));
        apiKey.setActive(false);
        apiKeyRepository.save(apiKey);
    }

    @Transactional(readOnly = true)
    public List<ApiKey> getAllKeys() {
        return apiKeyRepository.findAll();
    }

    @Transactional
    public void revokeExpiredKeys() {
        List<ApiKey> allKeys = apiKeyRepository.findAll();
        allKeys.stream()
                .filter(key -> !key.isValid())
                .forEach(key -> key.setActive(false));
        apiKeyRepository.saveAll(allKeys);
    }
}