package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.ApiKey;
import cc.jcguzman.petadoptionapi.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keys")
@RequiredArgsConstructor
@Validated
@Tag(name = "API Key Management", description = "Operations for managing API keys in the system")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @Schema(description = "Request object for generating a new API key")
    @Data
    public static class GenerateKeyRequest {
        @Schema(description = "Description of what this API key will be used for",
                example = "Development Testing Key",
                required = true)
        @NotBlank(message = "Description is required")
        @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
        private String description;

        @Schema(description = "Name or identifier of the person/system creating the key",
                example = "john.doe@example.com",
                required = true)
        @NotBlank(message = "Creator identifier is required")
        private String createdBy;

        @Schema(description = "Number of days the key should remain valid. Null means no expiration",
                example = "30",
                minimum = "1",
                maximum = "365")
        private Integer validityDays;
    }

    @Operation(
            summary = "Generate new API key",
            description = "Creates a new API key with specified parameters. This endpoint does not require authentication " +
                    "and should be used to generate the initial API key. The generated key should be stored securely " +
                    "as it cannot be retrieved later."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "API key generated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiKey.class),
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "keyValue": "pat_2024_11_04_abc123def456",
                                    "description": "Development Testing Key",
                                    "active": true,
                                    "createdAt": "2024-11-04T05:00:00Z",
                                    "expiresAt": "2024-12-04T05:00:00Z",
                                    "createdBy": "john.doe@example.com"
                                }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = """
                                    {
                                        "timestamp": "2024-11-04T05:00:00Z",
                                        "message": "Validation failed",
                                        "errors": [
                                            "Description must be between 5 and 200 characters",
                                            "Creator identifier is required"
                                        ]
                                    }
                                    """)
                    )
            )
    })
    @SecurityRequirements  // Indicates no security required
    @PostMapping("/generate")
    public ResponseEntity<ApiKey> generateKey(@Valid @RequestBody GenerateKeyRequest request) {
        ApiKey newKey = apiKeyService.generateKey(
                request.getDescription(),
                request.getCreatedBy(),
                request.getValidityDays()
        );
        return ResponseEntity.ok(newKey);
    }

    @Operation(
            summary = "List all API keys",
            description = "Retrieves a list of all API keys in the system. This includes both active and inactive keys. " +
                    "The actual key values are partially masked for security."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of API keys retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ApiKey.class)),
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "keyValue": "pat_2024_11_04_abc***",
                                        "description": "Development Key",
                                        "active": true,
                                        "createdAt": "2024-11-04T05:00:00Z",
                                        "expiresAt": null,
                                        "createdBy": "john.doe@example.com"
                                    },
                                    {
                                        "id": 2,
                                        "keyValue": "pat_2024_11_04_def***",
                                        "description": "Testing Key",
                                        "active": false,
                                        "createdAt": "2024-11-04T05:00:00Z",
                                        "expiresAt": "2024-12-04T05:00:00Z",
                                        "createdBy": "jane.smith@example.com"
                                    }
                                ]
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - API key is missing or invalid",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Invalid or missing API key\"}")
                    )
            )
    })
    @SecurityRequirement(name = "ApiKey")
    @GetMapping
    public ResponseEntity<List<ApiKey>> getAllKeys() {
        return ResponseEntity.ok(apiKeyService.getAllKeys());
    }

    @Operation(
            summary = "Revoke API key",
            description = "Deactivates an existing API key. Once revoked, the key can no longer be used to authenticate requests."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "API key revoked successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "API key not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"API key not found with id: 123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - API key is missing or invalid",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Invalid or missing API key\"}")
                    )
            )
    })
    @SecurityRequirement(name = "ApiKey")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeKey(
            @Parameter(description = "ID of the API key to revoke", example = "1", required = true)
            @PathVariable Long id) {
        apiKeyService.revokeKey(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Clean up expired keys",
            description = "Automatically deactivates all API keys that have passed their expiration date. " +
                    "This is typically run as a maintenance task."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cleanup completed successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - API key is missing or invalid",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Invalid or missing API key\"}")
                    )
            )
    })
    @SecurityRequirement(name = "ApiKey")
    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanupExpiredKeys() {
        apiKeyService.revokeExpiredKeys();
        return ResponseEntity.ok().build();
    }
}