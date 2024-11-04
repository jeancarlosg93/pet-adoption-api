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
public class Fosters {

    @JsonProperty("Fosters")
    private List<Foster> fosters;

    public static Fosters fromList(List<Foster> fosterList) {
        return new Fosters(new ArrayList<>(fosterList));
    }
}