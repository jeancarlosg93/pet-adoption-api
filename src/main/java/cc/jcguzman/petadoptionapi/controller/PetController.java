package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.model.Pets;
import cc.jcguzman.petadoptionapi.service.PetService;
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
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Tag(name = "Pet Management", description = "APIs for managing pets in the adoption system")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "ApiKey")
public class PetController {

    private final PetService petService;

    @Operation(
            summary = "Get all pets",
            description = "Retrieves a list of all pets in the system, including their details and current status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all pets",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pets.class)
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
            description = "Retrieves detailed information about a specific pet using their unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet found successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Pet not found with id: 123\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Pet not found with id: 123</message></error>")
                            )
                    }
            )
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> getPetById(
            @Parameter(description = "ID of the pet to retrieve", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @Operation(
            summary = "Get available pets",
            description = "Retrieves a list of all pets currently available for adoption"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved available pets",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/available", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pets> getAvailablePets() {
        List<Pet> availablePets = petService.getAvailablePets();
        return ResponseEntity.ok(Pets.of(availablePets));
    }

    @Operation(
            summary = "Get pets by species",
            description = "Retrieves a list of pets filtered by their species (e.g., Dog, Cat, Bird, etc.)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved pets by species",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/species/{species}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pets> getPetsBySpecies(
            @Parameter(description = "Species to filter by",
                    example = "Dog",
                    schema = @Schema(allowableValues = {"Dog", "Cat", "Bird", "Rabbit"}),
                    required = true)
            @PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(
            summary = "Get pets needing foster",
            description = "Retrieves a list of pets that currently need fostering (no assigned foster)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved pets needing foster",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pets.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/needs-foster", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pets> getPetsNeedingFoster() {
        List<Pet> pets = petService.getPetsNeedingFoster();
        return ResponseEntity.ok(Pets.of(pets));
    }

    @Operation(
            summary = "Create new pet",
            description = "Creates a new pet entry in the system with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pet created successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Validation failed\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Validation failed</message></error>")
                            )
                    }
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "Name": "Max",
                                                "Species": "Dog",
                                                "Breed": "Labrador",
                                                "Temperament": "Friendly",
                                                "Age": 3,
                                                "Gender": "Male",
                                                "Weight": 25.5,
                                                "Color": "Golden",
                                                "Adoption_Fee": 200.0
                                            }
                                            """
                            )
                    ),
                    @Content(
                            mediaType = MediaType.APPLICATION_XML_VALUE,
                            schema = @Schema(implementation = Pet.class),
                            examples = @ExampleObject(
                                    value = """
                                            <?xml version="1.0" encoding="UTF-8"?>
                                            <Pet>
                                                <Name>Max</Name>
                                                <Species>Dog</Species>
                                                <Breed>Labrador</Breed>
                                                <Temperament>Friendly</Temperament>
                                                <Age>3</Age>
                                                <Gender>Male</Gender>
                                                <Weight>25.5</Weight>
                                                <Color>Golden</Color>
                                                <AdoptionFee>200.0</AdoptionFee>
                                            </Pet>
                                            """
                            )
                    )
            }
    )
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update pet",
            description = "Updates an existing pet's information with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet updated successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Pet not found with id: 123\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Pet not found with id: 123</message></error>")
                            )
                    }
            )
    })
    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Pet> updatePet(
            @Parameter(description = "ID of the pet to update", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Pet petDetails) {
        return ResponseEntity.ok(petService.updatePet(id, petDetails));
    }

    @Operation(
            summary = "Update pet status",
            description = "Updates the status of an existing pet (e.g., AVAILABLE, FOSTERED, ADOPTED, REMOVED)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pet status updated successfully",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Pet not found with id: 123\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Pet not found with id: 123</message></error>")
                            )
                    }
            )
    })
    @PutMapping(
            value = "/{id}/status",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Pet> updatePetStatus(
            @Parameter(description = "ID of the pet to update", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status",
                    schema = @Schema(implementation = Pet.Status.class),
                    required = true)
            @RequestParam Pet.Status status) {
        return ResponseEntity.ok(petService.updatePetStatus(id, status));
    }

    @Operation(
            summary = "Remove pet",
            description = "Removes a pet from the system (soft delete - updates status to REMOVED)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Pet removed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2024-11-04T10:00:00\",\"message\":\"Pet not found with id: 123\"}")
                            ),
                            @Content(
                                    mediaType = MediaType.APPLICATION_XML_VALUE,
                                    schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error><timestamp>2024-11-04T10:00:00</timestamp><message>Pet not found with id: 123</message></error>")
                            )
                    }
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(
            @Parameter(description = "ID of the pet to remove", example = "1", required = true)
            @PathVariable Long id) {
        petService.removePet(id);
        return ResponseEntity.noContent().build();
    }
}