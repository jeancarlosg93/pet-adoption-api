package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Foster extends User {

    @JsonProperty("FosterSince")
    private Instant fosterSince = Instant.now();

    @JsonProperty("Active")
    private boolean active = true;

    @Min(1)
    @Max(5)
    @JsonProperty("MaxPets")
    private int maxPets = 3;

    @OneToMany(mappedBy = "currentFoster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonProperty("AssignedPets")
    private List<Pet> petsAssigned = new ArrayList<>();

    public void unassignPet(Pet pet) {
        petsAssigned.remove(pet);
        pet.setCurrentFoster(null);
    }

    public boolean canAcceptMorePets() {
        return active && petsAssigned.size() < maxPets;
    }

    public void assignPet(Pet pet) {
        if (canAcceptMorePets()) {
            petsAssigned.add(pet);
            pet.setCurrentFoster(this);
        } else {
            throw new IllegalStateException("Foster cannot accept more pets");
        }
    }
}