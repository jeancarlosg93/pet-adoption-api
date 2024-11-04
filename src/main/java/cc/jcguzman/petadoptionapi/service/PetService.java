package cc.jcguzman.petadoptionapi.service;

import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));
    }

    public List<Pet> getAvailablePets() {
        return petRepository.findByCurrentStatus(Pet.Status.AVAILABLE);
    }

    public List<Pet> getPetsBySpecies(String species) {
        return petRepository.findBySpecies(species);
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(UUID id, Pet petDetails) {
        Pet pet = getPetById(id);

        pet.setName(petDetails.getName());
        pet.setSpecies(petDetails.getSpecies());
        pet.setBreed(petDetails.getBreed());
        pet.setTemperament(petDetails.getTemperament());
        pet.setAge(petDetails.getAge());
        pet.setGender(petDetails.getGender());
        pet.setWeight(petDetails.getWeight());
        pet.setColor(petDetails.getColor());
        pet.setAdoptionFee(petDetails.getAdoptionFee());

        return petRepository.save(pet);
    }

    public void removePet(UUID id) {
        Pet pet = getPetById(id);
        pet.remove();
        petRepository.save(pet);
    }

    public List<Pet> getPetsNeedingFoster() {
        return petRepository.findByCurrentFosterIsNull();
    }

    public Pet updatePetStatus(UUID id, Pet.Status newStatus) {
        Pet pet = getPetById(id);
        pet.setCurrentStatus(newStatus);
        return petRepository.save(pet);
    }
}