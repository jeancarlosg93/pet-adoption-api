package cc.jcguzman.petadoptionapi.config;

import cc.jcguzman.petadoptionapi.model.Foster;
import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.repository.FosterRepository;
import cc.jcguzman.petadoptionapi.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PetRepository petRepository, FosterRepository fosterRepository) {
        return args -> {
            // Create dogs
            Pet dog1 = createPet("Max", "Dog", "Labrador", 3, "Male", 25.5, "Golden", "Friendly", 200.0);
            Pet dog2 = createPet("Bella", "Dog", "German Shepherd", 2, "Female", 28.0, "Black and Tan", "Protective", 250.0);
            Pet dog3 = createPet("Charlie", "Dog", "Golden Retriever", 1, "Male", 20.0, "Golden", "Playful", 300.0);
            Pet dog4 = createPet("Lucy", "Dog", "Beagle", 4, "Female", 12.5, "Tricolor", "Energetic", 175.0);
            Pet dog5 = createPet("Cooper", "Dog", "Husky", 2, "Male", 23.0, "Gray and White", "Independent", 275.0);

            // Create cats
            Pet cat1 = createPet("Luna", "Cat", "Siamese", 2, "Female", 4.5, "White and Gray", "Calm", 150.0);
            Pet cat2 = createPet("Oliver", "Cat", "Persian", 3, "Male", 5.0, "White", "Lazy", 200.0);
            Pet cat3 = createPet("Milo", "Cat", "Maine Coon", 1, "Male", 6.5, "Brown Tabby", "Gentle", 275.0);
            Pet cat4 = createPet("Sophie", "Cat", "Russian Blue", 2, "Female", 4.0, "Gray", "Shy", 225.0);
            Pet cat5 = createPet("Leo", "Cat", "Bengal", 1, "Male", 4.8, "Spotted Brown", "Active", 300.0);

            // Create other pets
            Pet rabbit1 = createPet("Thumper", "Rabbit", "Holland Lop", 1, "Male", 1.5, "White", "Docile", 100.0);
            Pet rabbit2 = createPet("Cotton", "Rabbit", "Dutch", 2, "Female", 2.0, "Black and White", "Friendly", 85.0);
            Pet bird1 = createPet("Rio", "Bird", "Parakeet", 1, "Male", 0.1, "Blue", "Cheerful", 50.0);
            Pet bird2 = createPet("Sunny", "Bird", "Cockatiel", 2, "Female", 0.15, "Yellow", "Musical", 75.0);

            // Save all pets
            petRepository.saveAll(List.of(
                    dog1, dog2, dog3, dog4, dog5,
                    cat1, cat2, cat3, cat4, cat5,
                    rabbit1, rabbit2, bird1, bird2
            ));

            // Create fosters
            Foster foster1 = createFoster("John", "Doe", "john.doe@example.com", "555-0123", "123 Main St", 3);
            Foster foster2 = createFoster("Jane", "Smith", "jane.smith@example.com", "555-0124", "456 Oak Ave", 2);
            Foster foster3 = createFoster("Robert", "Johnson", "robert.j@example.com", "555-0125", "789 Pine Rd", 4);
            Foster foster4 = createFoster("Maria", "Garcia", "maria.g@example.com", "555-0126", "321 Elm St", 2);
            Foster foster5 = createFoster("David", "Wilson", "david.w@example.com", "555-0127", "654 Maple Dr", 3);
            Foster foster6 = createFoster("Sarah", "Brown", "sarah.b@example.com", "555-0128", "987 Cedar Ln", 5);
            Foster foster7 = createFoster("Michael", "Taylor", "michael.t@example.com", "555-0129", "147 Birch Ave", 2);

            // Save all fosters
            fosterRepository.saveAll(List.of(
                    foster1, foster2, foster3, foster4,
                    foster5, foster6, foster7
            ));

            // Assign some pets to fosters
            assignPetToFoster(foster1, dog1);
            assignPetToFoster(foster1, cat1);
            assignPetToFoster(foster2, dog2);
            assignPetToFoster(foster3, cat2);
            assignPetToFoster(foster3, dog3);
            assignPetToFoster(foster3, rabbit1);
            assignPetToFoster(foster4, cat3);
            assignPetToFoster(foster6, bird1);
            assignPetToFoster(foster6, cat4);
            assignPetToFoster(foster6, dog4);

            // Save updated fosters
            fosterRepository.saveAll(List.of(
                    foster1, foster2, foster3, foster4,
                    foster5, foster6
            ));
        };
    }

    private Pet createPet(String name, String species, String breed, int age, String gender,
                          double weight, String color, String temperament, double adoptionFee) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setGender(gender);
        pet.setWeight(weight);
        pet.setColor(color);
        pet.setTemperament(temperament);
        pet.setAdoptionFee(adoptionFee);
        pet.setDateArrived(LocalDateTime.now());
        return pet;
    }

    private Foster createFoster(String name, String lastName, String email, String phone,
                                String address, int maxPets) {
        Foster foster = new Foster();
        foster.setName(name);
        foster.setLastName(lastName);
        foster.setEmail(email);
        foster.setPhone(phone);
        foster.setAddress(address);
        foster.setMaxPets(maxPets);
        return foster;
    }

    private void assignPetToFoster(Foster foster, Pet pet) {
        foster.assignPet(pet);
    }
}