package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.repository.PetRepository;

@Service
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Метод находит питомца по id
     *
     * @param id
     * @return Pet
     */
    public Pet findPet(int id){
        return petRepository.findById(id);
    }

}