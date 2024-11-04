package cc.jcguzman.petadoptionapi.controller;

import cc.jcguzman.petadoptionapi.model.Foster;
import cc.jcguzman.petadoptionapi.model.Fosters;
import cc.jcguzman.petadoptionapi.service.FosterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fosters")
@RequiredArgsConstructor
public class FosterController {

    private final FosterService fosterService;

    @GetMapping
    public ResponseEntity<Fosters> getAllFosters() {
        List<Foster> fosterList = fosterService.getAllFosters();
        return ResponseEntity.ok(new Fosters(fosterList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Foster> getFosterById(@PathVariable UUID id) {
        Foster foster = fosterService.getFosterById(id);
        return ResponseEntity.ok(foster);
    }

    @GetMapping("/active")
    public ResponseEntity<Fosters> getActiveFosters() {
        List<Foster> activeFosters = fosterService.getActiveFosters();
        return ResponseEntity.ok(new Fosters(activeFosters));
    }

    @GetMapping("/available")
    public ResponseEntity<Fosters> getAvailableFosters() {
        List<Foster> availableFosters = fosterService.getAvailableFosters();
        return ResponseEntity.ok(new Fosters(availableFosters));
    }

    @PostMapping
    public ResponseEntity<Foster> createFoster(@Valid @RequestBody Foster foster) {
        Foster createdFoster = fosterService.createFoster(foster);
        return new ResponseEntity<>(createdFoster, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Foster> updateFoster(@PathVariable UUID id, @Valid @RequestBody Foster fosterDetails) {
        Foster updatedFoster = fosterService.updateFoster(id, fosterDetails);
        return ResponseEntity.ok(updatedFoster);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateFoster(@PathVariable UUID id) {
        fosterService.deactivateFoster(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{fosterId}/pets/{petId}")
    public ResponseEntity<Foster> assignPetToFoster(
            @PathVariable UUID fosterId,
            @PathVariable UUID petId) {
        Foster updatedFoster = fosterService.assignPetToFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }

    @DeleteMapping("/{fosterId}/pets/{petId}")
    public ResponseEntity<Foster> unassignPetFromFoster(
            @PathVariable UUID fosterId,
            @PathVariable UUID petId) {
        Foster updatedFoster = fosterService.unassignPetFromFoster(fosterId, petId);
        return ResponseEntity.ok(updatedFoster);
    }
}