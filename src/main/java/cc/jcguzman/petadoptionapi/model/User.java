package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("UUID")
    private UUID id;

    @NotBlank(message = "Name is required")
    @JsonProperty("Name")
    private String name;

    @NotBlank(message = "Last name is required")
    @JsonProperty("Last Name")
    private String lastName;

    @NotBlank(message = "Phone is required")
    @JsonProperty("Phone")
    private String phone;

    @NotBlank(message = "Address is required")
    @JsonProperty("Address")
    private String address;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @JsonProperty("Email")
    private String email;
}