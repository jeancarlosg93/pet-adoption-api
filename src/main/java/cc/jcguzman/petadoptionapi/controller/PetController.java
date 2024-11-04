package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.model.Pets;
import cc.jcguzman.petadoptionapi.service.PetService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Tag(name = "Pet Management", description = "APIs for managing pets in the adoption system")
public class PetController {

    private final PetService petService;

    @Operation(
            summary = "Get all pets",
            description = "Retrieves a list of all pets in the system regardless of their status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all pets",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pets.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping
    public ResponseEntity<Pets> getAllPets() {
        List<Pet> petList = petService.getAllPets();
        return ResponseEntity.ok(new Pets(petList));
    }

    @Operation(
            summary = "Get pet by ID",
            description = "Retrieves a specific pet by their UUID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(
            @Parameter(description = "UUID of the pet to retrieve")
            @PathVariable UUID id) {
        Pet pet = petService.getPetById(id);
        return ResponseEntity.ok(pet);
    }

    @Operation(
            summary = "Get available pets",
            description = "Retrieves a list of all pets that are available for adoption"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved available pets",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pets.class)
                    )
            )
    })
    @GetMapping("/available")
    public ResponseEntity<Pets> getAvailablePets() {
        List<Pet> availablePets = petService.getAvailablePets();
        return ResponseEntity.ok(new Pets(availablePets));
    }

    @Operation(
            summary = "Get pets by species",
            description = "Retrieves all pets of a specific species (e.g., 'Dog', 'Cat')"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved pets by species",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pets.class)
                    )
            )
    })
    @GetMapping("/species/{species}")
    public ResponseEntity<Pets> getPetsBySpecies(
            @Parameter(description = "Species of pets to retrieve (e.g., 'Dog', 'Cat')")
            @PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(new Pets(pets));
    }

    @Operation(
            summary = "Get pets needing foster",
            description = "Retrieves a list of all pets that need foster homes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved pets needing foster care",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pets.class)
                    )
            )
    })
    @GetMapping("/needs-foster")
    public ResponseEntity<Pets> getPetsNeedingFoster() {
        List<Pet> pets = petService.getPetsNeedingFoster();
        return ResponseEntity.ok(new Pets(pets));
    }

    @Operation(
            summary = "Create new pet",
            description = "Registers a new pet in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pet created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping
    public ResponseEntity<Pet> createPet(
            @Parameter(description = "Pet details", required = true)
            @Valid @RequestBody Pet pet) {
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update pet",
            description = "Updates an existing pet's information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @Parameter(description = "UUID of the pet to update")
            @PathVariable UUID id,
            @Parameter(description = "Updated pet details", required = true)
            @Valid @RequestBody Pet petDetails) {
        Pet updatedPet = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(
            summary = "Update pet status",
            description = "Updates the status of a pet (AVAILABLE, FOSTERED, ADOPTED, REMOVED)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet status updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Pet> updatePetStatus(
            @Parameter(description = "UUID of the pet")
            @PathVariable UUID id,
            @Parameter(
                    description = "New status for the pet",
                    schema = @Schema(implementation = Pet.Status.class)
            )
            @RequestParam Pet.Status status) {
        Pet updatedPet = petService.updatePetStatus(id, status);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(
            summary = "Remove pet",
            description = "Removes a pet from the system (soft delete by changing status to REMOVED)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Pet removed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(
            @Parameter(description = "UUID of the pet to remove")
            @PathVariable UUID id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }
}