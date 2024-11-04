package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Name")
    @Column(nullable = false)
    private String name;

    @JsonProperty("Species")
    @Column(nullable = false)
    private String species;

    @JsonProperty("Breed")
    private String breed;

    @JsonProperty("Temperament")
    private String temperament;

    @JsonProperty("Age")
    private int age;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Weight")
    private double weight;

    @JsonProperty("Color")
    private String color;

    @JsonProperty("Date_Arrived")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateArrived = LocalDateTime.now();

    @JsonProperty("Adoption_Fee")
    private double adoptionFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foster_id")
    @JsonIgnoreProperties({"petsAssigned", "hibernateLazyInitializer"})
    @JsonIgnore
    @ToString.Exclude
    private Foster currentFoster;

    @Enumerated(EnumType.STRING)
    @JsonProperty("Current_Status")
    private Status currentStatus = Status.AVAILABLE;

    public enum Status {
        FOSTERED,
        AVAILABLE,
        ADOPTED,
        REMOVED
    }

    @JsonProperty("Foster_Id")
    public Long getFosterId() {
        return currentFoster != null ? currentFoster.getId() : null;
    }

    @JsonProperty("Foster_Name")
    public String getFosterName() {
        return currentFoster != null ? currentFoster.getName() + " " + currentFoster.getLastName() : null;
    }

    public void remove() {
        if (currentFoster != null) {
            Foster foster = currentFoster;
            currentFoster = null;  // Break the bidirectional relationship
            foster.unassignPet(this);
        }
        currentStatus = Status.REMOVED;
    }
}