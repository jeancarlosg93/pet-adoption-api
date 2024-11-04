package cc.jcguzman.petadoptionapi.repository;

import cc.jcguzman.petadoptionapi.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByCurrentStatus(Pet.Status status);
    List<Pet> findBySpecies(String species);
    List<Pet> findByBreed(String breed);
    List<Pet> findByCurrentFosterIsNull();
}