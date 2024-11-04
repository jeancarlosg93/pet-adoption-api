package cc.jcguzman.petadoptionapi.service;

import cc.jcguzman.petadoptionapi.model.Foster;
import cc.jcguzman.petadoptionapi.model.Pet;
import cc.jcguzman.petadoptionapi.repository.FosterRepository;
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
public class FosterService {

    private final FosterRepository fosterRepository;
    private final PetRepository petRepository;

    public List<Foster> getAllFosters() {
        return fosterRepository.findAll();
    }

    public Foster getFosterById(UUID id) {
        return fosterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Foster not found with id: " + id));
    }

    public List<Foster> getActiveFosters() {
        return fosterRepository.findByActiveTrue();
    }

    public List<Foster> getAvailableFosters() {
        return fosterRepository.findAvailableFosters();
    }

    public Foster createFoster(Foster foster) {
        if (fosterRepository.existsByEmail(foster.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        return fosterRepository.save(foster);
    }

    public Foster updateFoster(UUID id, Foster fosterDetails) {
        Foster foster = getFosterById(id);

        foster.setName(fosterDetails.getName());
        foster.setLastName(fosterDetails.getLastName());
        foster.setPhone(fosterDetails.getPhone());
        foster.setAddress(fosterDetails.getAddress());
        foster.setEmail(fosterDetails.getEmail());
        foster.setActive(fosterDetails.isActive());
        foster.setMaxPets(fosterDetails.getMaxPets());

        return fosterRepository.save(foster);
    }

    public void deactivateFoster(UUID id) {
        Foster foster = getFosterById(id);
        foster.setActive(false);
        fosterRepository.save(foster);
    }

    public Foster assignPetToFoster(UUID fosterId, UUID petId) {
        Foster foster = getFosterById(fosterId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        if (!foster.isActive()) {
            throw new IllegalStateException("Foster is not active");
        }

        if (!foster.canAcceptMorePets()) {
            throw new IllegalStateException("Foster has reached maximum pet capacity");
        }

        foster.assignPet(pet);
        return fosterRepository.save(foster);
    }

    public Foster unassignPetFromFoster(UUID fosterId, UUID petId) {
        Foster foster = getFosterById(fosterId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        foster.unassignPet(pet);
        return fosterRepository.save(foster);
    }
}