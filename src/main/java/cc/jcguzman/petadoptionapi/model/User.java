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
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Last name is required")
    @JsonProperty("Last Name")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Phone is required")
    @JsonProperty("Phone")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Address is required")
    @JsonProperty("Address")
    @Column(nullable = false)
    private String address;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @JsonProperty("Email")
    @Column(nullable = false, unique = true)
    private String email;
}