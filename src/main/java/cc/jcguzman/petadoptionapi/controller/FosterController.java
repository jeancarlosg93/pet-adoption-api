package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.Foster;
import cc.jcguzman.petadoptionapi.model.Fosters;
import cc.jcguzman.petadoptionapi.service.FosterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
public class FosterController {

    private final FosterService fosterService;

    @Operation(
            summary = "Get all fosters",
            description = "Retrieves a list of all foster caregivers registered in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all fosters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Fosters.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping
    public ResponseEntity<Fosters> getAllFosters() {
        List<Foster> fosterList = fosterService.getAllFosters();
        return ResponseEntity.ok(new Fosters(fosterList));
    }

    @Operation(
            summary = "Get foster by ID",
            description = "Retrieves a specific foster caregiver by their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Foster found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Foster> getFosterById(
            @Parameter(description = "ID of the foster to retrieve")
            @PathVariable Long id) {
        Foster foster = fosterService.getFosterById(id);
        return ResponseEntity.ok(foster);
    }

    @Operation(
            summary = "Get active fosters",
            description = "Retrieves a list of all active foster caregivers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved active fosters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Fosters.class)
                    )
            )
    })
    @GetMapping("/active")
    public ResponseEntity<Fosters> getActiveFosters() {
        List<Foster> activeFosters = fosterService.getActiveFosters();
        return ResponseEntity.ok(new Fosters(activeFosters));
    }

    @Operation(
            summary = "Get available fosters",
            description = "Retrieves a list of active fosters who can accept more pets"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved available fosters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Fosters.class)
                    )
            )
    })
    @GetMapping("/available")
    public ResponseEntity<Fosters> getAvailableFosters() {
        List<Foster> availableFosters = fosterService.getAvailableFosters();
        return ResponseEntity.ok(new Fosters(availableFosters));
    }

    @Operation(
            summary = "Create new foster",
            description = "Creates a new foster caregiver in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Foster created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already registered",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping
    public ResponseEntity<Foster> createFoster(
            @Parameter(description = "Foster details", required = true)
            @Valid @RequestBody Foster foster) {
        Foster createdFoster = fosterService.createFoster(foster);
        return new ResponseEntity<>(createdFoster, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update foster",
            description = "Updates an existing foster's information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Foster updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Foster> updateFoster(
            @Parameter(description = "ID of the foster to update")
            @PathVariable Long id,
            @Parameter(description = "Updated foster details", required = true)
            @Valid @RequestBody Foster fosterDetails) {
        Foster updatedFoster = fosterService.updateFoster(id, fosterDetails);
        return ResponseEntity.ok(updatedFoster);
    }

    @Operation(
            summary = "Deactivate foster",
            description = "Deactivates a foster caregiver (soft delete)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Foster deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateFoster(
            @Parameter(description = "ID of the foster to deactivate")
            @PathVariable Long id) {
        fosterService.deactivateFoster(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Assign pet to foster",
            description = "Assigns a pet to a foster caregiver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet assigned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster or pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Foster cannot accept more pets or is inactive",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/{fosterId}/pets/{petId}")
    public ResponseEntity<Foster> assignPetToFoster(
            @Parameter(description = "ID of the foster")
            @PathVariable Long fosterId,
            @Parameter(description = "ID of the pet to assign")
            @PathVariable Long petId) {
        Foster updatedFoster = fosterService.assignPetToFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }

    @Operation(
            summary = "Remove pet from foster",
            description = "Removes a pet from a foster's care"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet removed successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Foster.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foster or pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @DeleteMapping("/{fosterId}/pets/{petId}")
    public ResponseEntity<Foster> unassignPetFromFoster(
            @Parameter(description = "ID of the foster")
            @PathVariable Long fosterId,
            @Parameter(description = "ID of the pet to remove")
            @PathVariable Long petId) {
        Foster updatedFoster = fosterService.unassignPetFromFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }
}