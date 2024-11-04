package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("UUID")
    private UUID id;

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
    @JsonProperty("Current_Foster")
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

    public void remove() {
        if (currentFoster != null) {
            currentFoster.unassignPet(this);
        }
        currentStatus = Status.REMOVED;
    }
}