package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pets {

    @JsonProperty("pets")
    private List<Pet> petList;

    public static Pets of(List<Pet> pets) {
        return new Pets(new ArrayList<>(pets));
    }
}