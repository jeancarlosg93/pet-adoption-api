package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pets {

    @JsonProperty("Pets")
    private List<Pet> petList;

    // For backward compatibility
    public Pets(HashMap<UUID, Pet> pets) {
        this.petList = new ArrayList<>(pets.values());
    }
}