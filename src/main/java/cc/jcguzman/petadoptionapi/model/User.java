package cc.jcguzman.petadoptionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    private Long id;

    @NotBlank(message = "Name is required")
    @JsonProperty("Name")
    @JacksonXmlProperty(localName = "Name")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Last name is required")
    @JsonProperty("Last Name")
    @JacksonXmlProperty(localName = "LastName")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Phone is required")
    @JsonProperty("Phone")
    @JacksonXmlProperty(localName = "Phone")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Address is required")
    @JsonProperty("Address")
    @JacksonXmlProperty(localName = "Address")
    @Column(nullable = false)
    private String address;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @JsonProperty("Email")
    @JacksonXmlProperty(localName = "Email")
    @Column(nullable = false, unique = true)
    private String email;
}