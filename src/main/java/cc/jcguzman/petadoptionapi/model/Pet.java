package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    private Long id;

    @JsonProperty("Name")
    @JacksonXmlProperty(localName = "Name")
    @Column(nullable = false)
    private String name;

    @JsonProperty("Species")
    @JacksonXmlProperty(localName = "Species")
    @Column(nullable = false)
    private String species;

    @JsonProperty("Breed")
    @JacksonXmlProperty(localName = "Breed")
    private String breed;

    @JsonProperty("Temperament")
    @JacksonXmlProperty(localName = "Temperament")
    private String temperament;

    @JsonProperty("Age")
    @JacksonXmlProperty(localName = "Age")
    private int age;

    @JsonProperty("Gender")
    @JacksonXmlProperty(localName = "Gender")
    private String gender;

    @JsonProperty("Weight")
    @JacksonXmlProperty(localName = "Weight")
    private double weight;

    @JsonProperty("Color")
    @JacksonXmlProperty(localName = "Color")
    private String color;

    @JsonProperty("Date_Arrived")
    @JacksonXmlProperty(localName = "DateArrived")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateArrived = LocalDateTime.now();

    @JsonProperty("Adoption_Fee")
    @JacksonXmlProperty(localName = "AdoptionFee")
    private double adoptionFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foster_id")
    @JsonIgnoreProperties({"petsAssigned", "hibernateLazyInitializer"})
    @JsonIgnore
    @ToString.Exclude
    private Foster currentFoster;

    @Enumerated(EnumType.STRING)
    @JsonProperty("Current_Status")
    @JacksonXmlProperty(localName = "CurrentStatus")
    private Status currentStatus = Status.AVAILABLE;

    public enum Status {
        FOSTERED,
        AVAILABLE,
        ADOPTED,
        REMOVED
    }

    @JsonProperty("Foster_Id")
    @JacksonXmlProperty(localName = "FosterId")
    public Long getFosterId() {
        return currentFoster != null ? currentFoster.getId() : null;
    }

    @JsonProperty("Foster_Name")
    @JacksonXmlProperty(localName = "FosterName")
    public String getFosterName() {
        return currentFoster != null ? currentFoster.getName() + " " + currentFoster.getLastName() : null;
    }

    public void remove() {
        if (currentFoster != null) {
            Foster foster = currentFoster;
            currentFoster = null;
            foster.unassignPet(this);
        }
        currentStatus = Status.REMOVED;
    }
}