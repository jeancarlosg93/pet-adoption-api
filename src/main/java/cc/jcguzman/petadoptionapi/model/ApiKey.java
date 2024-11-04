package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Schema(description = "Represents an API key in the system")
@Entity
@Table(name = "api_keys")
@Data
@NoArgsConstructor
public class ApiKey {

    @Schema(description = "Unique identifier for the API key", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            description = "The actual API key value. This value is only shown in full when first generated",
            example = "pat_2024_11_04_abc123def456"
    )
    @Column(nullable = false, unique = true)
    private String keyValue;

    @Schema(
            description = "Description of what this API key is used for",
            example = "Development Testing Key"
    )
    @Column(nullable = false)
    private String description;

    @Schema(
            description = "Whether this API key is currently active",
            example = "true",
            defaultValue = "true"
    )
    @Column(nullable = false)
    private boolean active = true;

    @Schema(
            description = "Timestamp when this API key was created",
            example = "2024-11-04T05:00:00Z"
    )
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Schema(
            description = "Timestamp when this API key will expire. Null means no expiration",
            example = "2024-12-04T05:00:00Z",
            nullable = true
    )
    @Column
    private Instant expiresAt;

    @Schema(
            description = "Identifier of who created this API key",
            example = "john.doe@example.com"
    )
    @Column(nullable = false)
    private String createdBy;

    @Schema(
            description = "Checks if the API key is currently valid (active and not expired)",
            example = "true"
    )
    @JsonProperty("isValid")
    public boolean isValid() {
        return active && (expiresAt == null || expiresAt.isAfter(Instant.now()));
    }
}