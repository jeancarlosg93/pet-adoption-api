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

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PetRepository petRepository, FosterRepository fosterRepository) {
        return args -> {
            // Create some test pets
            Pet dog1 = new Pet();
            dog1.setName("Max");
            dog1.setSpecies("Dog");
            dog1.setBreed("Labrador");
            dog1.setAge(3);
            dog1.setGender("Male");
            dog1.setWeight(25.5);
            dog1.setColor("Golden");
            dog1.setTemperament("Friendly");
            dog1.setAdoptionFee(200.0);
            dog1.setDateArrived(LocalDateTime.now());

            Pet cat1 = new Pet();
            cat1.setName("Luna");
            cat1.setSpecies("Cat");
            cat1.setBreed("Siamese");
            cat1.setAge(2);
            cat1.setGender("Female");
            cat1.setWeight(4.5);
            cat1.setColor("White and Gray");
            cat1.setTemperament("Calm");
            cat1.setAdoptionFee(150.0);
            cat1.setDateArrived(LocalDateTime.now());

            petRepository.save(dog1);
            petRepository.save(cat1);

            // Create some test fosters
            Foster foster1 = new Foster();
            foster1.setName("John");
            foster1.setLastName("Doe");
            foster1.setEmail("john.doe@example.com");
            foster1.setPhone("555-0123");
            foster1.setAddress("123 Main St");
            foster1.setMaxPets(3);

            Foster foster2 = new Foster();
            foster2.setName("Jane");
            foster2.setLastName("Smith");
            foster2.setEmail("jane.smith@example.com");
            foster2.setPhone("555-0124");
            foster2.setAddress("456 Oak Ave");
            foster2.setMaxPets(2);

            fosterRepository.save(foster1);
            fosterRepository.save(foster2);
        };
    }
}