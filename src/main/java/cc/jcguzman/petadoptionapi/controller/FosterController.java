package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.Foster;
import cc.jcguzman.petadoptionapi.model.Fosters;
import cc.jcguzman.petadoptionapi.service.FosterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fosters")
@RequiredArgsConstructor
@Tag(name = "Foster Management", description = "APIs for managing foster caregivers in the pet adoption system")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "ApiKey")
public class FosterController {

    private final FosterService fosterService;

    @Operation(
            summary = "Get all fosters",
            description = "Retrieves a list of all foster caregivers registered in the system, including their assigned pets"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all fosters",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Internal server error\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Internal server error</message></error>")
                            )
                    }
            )
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Fosters> getAllFosters() {
        List<Foster> fosterList = fosterService.getAllFosters();
        return ResponseEntity.ok(new Fosters(fosterList));
    }

    @Operation(
            summary = "Get foster by ID",
            description = "Retrieves detailed information about a specific foster caregiver using their unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Foster found successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Foster not found with id: 123\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Foster not found with id: 123</message></error>")
                            )
                    }
            )
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Foster> getFosterById(
            @Parameter(description = "ID of the foster to retrieve", example = "1", required = true)
            @PathVariable Long id) {
        Foster foster = fosterService.getFosterById(id);
        return ResponseEntity.ok(foster);
    }

    @Operation(
            summary = "Get active fosters",
            description = "Retrieves a list of all active foster caregivers in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved active fosters",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/active", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Fosters> getActiveFosters() {
        List<Foster> activeFosters = fosterService.getActiveFosters();
        return ResponseEntity.ok(new Fosters(activeFosters));
    }

    @Operation(
            summary = "Get available fosters",
            description = "Retrieves a list of active fosters who can accept more pets based on their maximum capacity"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved available fosters",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Fosters.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/available", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Fosters> getAvailableFosters() {
        List<Foster> availableFosters = fosterService.getAvailableFosters();
        return ResponseEntity.ok(new Fosters(availableFosters));
    }

    @Operation(
            summary = "Create new foster",
            description = "Creates a new foster caregiver in the system with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Foster created successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already registered"
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "Name": "John",
                                        "Last Name": "Doe",
                                        "Email": "john.doe@example.com",
                                        "Phone": "555-0123",
                                        "Address": "123 Main St",
                                        "MaxPets": 3,
                                        "Active": true
                                    }
                                    """)
                    ),
                    @Content(
                            mediaType = MediaType.APPLICATION_XML_VALUE,
                            schema = @Schema(implementation = Foster.class),
                            examples = @ExampleObject(value = """
                                    <?xml version="1.0" encoding="UTF-8"?>
                                    <Foster>
                                        <Name>John</Name>
                                        <LastName>Doe</LastName>
                                        <Email>john.doe@example.com</Email>
                                        <Phone>555-0123</Phone>
                                        <Address>123 Main St</Address>
                                        <MaxPets>3</MaxPets>
                                        <Active>true</Active>
                                    </Foster>
                                    """)
                    )
            }
    )
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Foster> createFoster(@Valid @RequestBody Foster foster) {
        Foster createdFoster = fosterService.createFoster(foster);
        return new ResponseEntity<>(createdFoster, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update foster",
            description = "Updates an existing foster's information with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Foster updated successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Foster not found")
    })
    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Foster> updateFoster(
            @Parameter(description = "ID of the foster to update", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Foster fosterDetails) {
        Foster updatedFoster = fosterService.updateFoster(id, fosterDetails);
        return ResponseEntity.ok(updatedFoster);
    }

    @Operation(
            summary = "Deactivate foster",
            description = "Deactivates a foster caregiver (soft delete) and removes all pet assignments"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Foster deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Foster not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateFoster(
            @Parameter(description = "ID of the foster to deactivate", example = "1", required = true)
            @PathVariable Long id) {
        fosterService.deactivateFoster(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Assign pet to foster",
            description = "Assigns a pet to a foster caregiver if they have available capacity"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet assigned successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Foster or pet not found"),
            @ApiResponse(responseCode = "400", description = "Foster cannot accept more pets or is inactive")
    })
    @PostMapping(
            value = "/{fosterId}/pets/{petId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Foster> assignPetToFoster(
            @Parameter(description = "ID of the foster", example = "1", required = true)
            @PathVariable Long fosterId,
            @Parameter(description = "ID of the pet to assign", example = "1", required = true)
            @PathVariable Long petId) {
        Foster updatedFoster = fosterService.assignPetToFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }

    @Operation(
            summary = "Remove pet from foster",
            description = "Removes a pet from a foster's care and updates the pet's status to AVAILABLE"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet removed successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Foster.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Foster or pet not found"),
            @ApiResponse(responseCode = "400", description = "Pet is not assigned to this foster")
    })
    @DeleteMapping(
            value = "/{fosterId}/pets/{petId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Foster> unassignPetFromFoster(
            @Parameter(description = "ID of the foster", example = "1", required = true)
            @PathVariable Long fosterId,
            @Parameter(description = "ID of the pet to remove", example = "1", required = true)
            @PathVariable Long petId) {
        Foster updatedFoster = fosterService.unassignPetFromFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }
}