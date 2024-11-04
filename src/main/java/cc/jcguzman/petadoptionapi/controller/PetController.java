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

    @Operation(summary = "Get all pets")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pets> getAllPets() {
        try {
            List<Pet> petList = petService.getAllPets();
            return ResponseEntity.ok(Pets.of(petList));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving pets", e);
        }
    }

    @Operation(summary = "Get pet by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @Operation(summary = "Get available pets")
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pets> getAvailablePets() {
        List<Pet> availablePets = petService.getAvailablePets();
        return ResponseEntity.ok(Pets.of(availablePets));
    }

    @Operation(summary = "Get pets by species")
    @GetMapping(value = "/species/{species}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pets> getPetsBySpecies(@PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(summary = "Get pets needing foster")
    @GetMapping(value = "/needs-foster", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pets> getPetsNeedingFoster() {
        List<Pet> pets = petService.getPetsNeedingFoster();
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(summary = "Create new pet")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @Operation(summary = "Update pet")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(
            @PathVariable UUID id,
            @Valid @RequestBody Pet petDetails) {
        return ResponseEntity.ok(petService.updatePet(id, petDetails));
    }

    @Operation(summary = "Update pet status")
    @PutMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePetStatus(
            @PathVariable UUID id,
            @RequestParam Pet.Status status) {
        return ResponseEntity.ok(petService.updatePetStatus(id, status));
    }

    @Operation(summary = "Remove pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(@PathVariable UUID id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }
}