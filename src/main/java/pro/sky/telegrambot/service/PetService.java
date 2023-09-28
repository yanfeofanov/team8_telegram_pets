package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.Collection;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Метод находит питомца по id владельца
     *
     * @param id уникальный идентификатор питомца
     * @return Pet
     */
    public Pet findPetById(int id) {
        return petRepository.findById(id);
    }

    public Pet addPet(Pet newPet) {
        return petRepository.save(newPet);
    }

    public void deletePet(int id) {
        petRepository.deleteById(id);
    }

    public Collection<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Collection<Pet> getAllPetsByType(TypeOfPet typeOfPet) {
        return petRepository.findAllByType(typeOfPet.toString());
    }

    public Collection<Pet> findPetsByPetOwnerId(int id) {
        return petRepository.findAllByPetOwnerId(id);
    }

    public Collection<Pet> getAllPetsThatHaveLeftShelter() {
        return petRepository.findAllByLeaveTrue();
    }

    public Collection<Pet> getAllAvailable() {
        return petRepository.findAllByLeaveFalseAndPetOwnerIsNull();
    }

    public Collection<Pet> gatAllOnProbation() {
        return petRepository.findAllByLeaveFalseAndPetOwnerIsNotNull();
    }

    public Collection<Pet> getAllAvailablePetsByType(TypeOfPet typeOfPet) {
        return petRepository.findAllByTypeAndLeaveIsFalseAndPetOwnerIsNull(typeOfPet.toString());
    }

    public Collection<Pet> getAllPetsOnProbationByType(TypeOfPet typeOfPet) {
        return petRepository.findAllByTypeAndLeaveIsFalseAndPetOwnerIsNotNull(typeOfPet.toString());
    }

    /**
     * Метод находит питомца на испытательном сроке по id владельца
     *
     * @param id уникальный идентификатор владельца питомца
     * @return Pet
     */
    public Pet findPetOnProbationByPetOwnerId(int id) {
        return petRepository.getFirstByPetOwnerIdAndLeaveIsFalse(id);
    }
}