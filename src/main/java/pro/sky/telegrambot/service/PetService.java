package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.repository.PetRepository;

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

    /**
     * Метод находит питомца по id владельца
     *
     * @param petOwnerId уникальный идентификатор владельца питомца
     * @return Pet
     */
    public Pet findPetByPetOwnerId(int petOwnerId){
        return petRepository.findByPetOwnerIdAndLeaveFalse(petOwnerId);
    }

    public Pet addPet(Pet newPet) {
        return petRepository.save(newPet);
    }
}