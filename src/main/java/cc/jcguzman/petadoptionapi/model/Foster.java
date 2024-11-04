package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fosters")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Foster extends User {

    @JsonProperty("FosterSince")
    @Column(nullable = false)
    private Instant fosterSince = Instant.now();

    @JsonProperty("Active")
    @Column(nullable = false)
    private boolean active = true;

    @Min(1)
    @Max(5)
    @JsonProperty("MaxPets")
    @Column(nullable = false)
    private int maxPets = 3;

    @OneToMany(mappedBy = "currentFoster", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("AssignedPets")
    @ToString.Exclude
    @JsonIgnoreProperties("currentFoster")
    private List<Pet> petsAssigned = new ArrayList<>();

    public void unassignPet(Pet pet) {
        if (petsAssigned.remove(pet)) {
            pet.setCurrentFoster(null);
            pet.setCurrentStatus(Pet.Status.AVAILABLE);
        }
    }

    public boolean canAcceptMorePets() {
        return active && petsAssigned.size() < maxPets;
    }

    public void assignPet(Pet pet) {
        if (!canAcceptMorePets()) {
            throw new IllegalStateException("Foster cannot accept more pets");
        }

        // If pet already has a foster, remove it from that foster first
        if (pet.getCurrentFoster() != null && pet.getCurrentFoster() != this) {
            pet.getCurrentFoster().unassignPet(pet);
        }

        petsAssigned.add(pet);
        pet.setCurrentFoster(this);
        pet.setCurrentStatus(Pet.Status.FOSTERED);
    }

    @JsonProperty("CurrentPetCount")
    public int getCurrentPetCount() {
        return petsAssigned.size();
    }

    @PreRemove
    private void preRemove() {
        for (Pet pet : new ArrayList<>(petsAssigned)) {
            unassignPet(pet);
        }
    }
}