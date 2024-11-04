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

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Tag(name = "Pet Management", description = "APIs for managing pets in the adoption system")
public class PetController {

    private final PetService petService;

    @Operation(
            summary = "Get all pets",
            description = "Retrieves a list of all pets in the system"
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
        try {
            List<Pet> petList = petService.getAllPets();
            return ResponseEntity.ok(Pets.of(petList));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving pets", e);
        }
    }

    @Operation(
            summary = "Get pet by ID",
            description = "Retrieves a specific pet by their ID"
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
            @Parameter(description = "ID of the pet to retrieve")
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @Operation(
            summary = "Get available pets",
            description = "Retrieves a list of all available pets for adoption"
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
        return ResponseEntity.ok(Pets.of(availablePets));
    }

    @Operation(
            summary = "Get pets by species",
            description = "Retrieves a list of pets filtered by species"
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
            @Parameter(description = "Species to filter by (e.g., 'Dog', 'Cat')")
            @PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(
            summary = "Get pets needing foster",
            description = "Retrieves a list of pets that need fostering"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved pets needing foster",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pets.class)
                    )
            )
    })
    @GetMapping("/needs-foster")
    public ResponseEntity<Pets> getPetsNeedingFoster() {
        List<Pet> pets = petService.getPetsNeedingFoster();
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(
            summary = "Create new pet",
            description = "Creates a new pet in the system"
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
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @Parameter(description = "ID of the pet to update")
            @PathVariable Long id,
            @Parameter(description = "Updated pet details", required = true)
            @Valid @RequestBody Pet petDetails) {
        return ResponseEntity.ok(petService.updatePet(id, petDetails));
    }

    @Operation(
            summary = "Update pet status",
            description = "Updates the status of an existing pet"
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
            )
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Pet> updatePetStatus(
            @Parameter(description = "ID of the pet to update")
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam Pet.Status status) {
        return ResponseEntity.ok(petService.updatePetStatus(id, status));
    }

    @Operation(
            summary = "Remove pet",
            description = "Removes a pet from the system"
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
            @Parameter(description = "ID of the pet to remove")
            @PathVariable Long id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }
}