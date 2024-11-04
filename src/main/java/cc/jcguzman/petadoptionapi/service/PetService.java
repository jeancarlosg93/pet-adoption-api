package cc.jcguzman.petadoptionapi.service;

import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Long id) {
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

    public Pet updatePet(Long id, Pet petDetails) {
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

    public void removePet(Long id) {
        Pet pet = getPetById(id);
        pet.remove();
        petRepository.save(pet);
    }

    public List<Pet> getPetsNeedingFoster() {
        return petRepository.findByCurrentFosterIsNull();
    }

    public Pet updatePetStatus(Long id, Pet.Status newStatus) {
        Pet pet = getPetById(id);
        pet.setCurrentStatus(newStatus);
        return petRepository.save(pet);
    }
}