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
            description = "Retrieves a list of all pets in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all pets"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
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
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        Pet pet = petService.getPetById(id);
        return ResponseEntity.ok(pet);
    }
    @Operation(
            summary = "Get pet by availability",
            description = "Retrieves a specific pet by their Available status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/available")
    public ResponseEntity<Pets> getAvailablePets() {
        List<Pet> availablePets = petService.getAvailablePets();
        return ResponseEntity.ok(new Pets(availablePets));
    }

    @Operation(
            summary = "Get pet by species",
            description = "Retrieves a specific pet by their species"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/species/{species}")
    public ResponseEntity<Pets> getPetsBySpecies(@PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(new Pets(pets));
    }

    @Operation(
            summary = "Get pet by  in need of foster",
            description = "Retrieves a specific pet if not fostered"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/needs-foster")
    public ResponseEntity<Pets> getPetsNeedingFoster() {
        List<Pet> pets = petService.getPetsNeedingFoster();
        return ResponseEntity.ok(new Pets(pets));
    }

    @Operation(
            summary = "Create new pet",
            description = "Creates a new pet in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pet successfully created",
                    content = @Content(schema = @Schema(implementation = Pet.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @Valid @RequestBody Pet petDetails) {
        Pet updatedPet = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(updatedPet);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pet> updatePetStatus(
            @PathVariable UUID id,
            @RequestParam Pet.Status status) {
        Pet updatedPet = petService.updatePetStatus(id, status);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(@PathVariable UUID id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}