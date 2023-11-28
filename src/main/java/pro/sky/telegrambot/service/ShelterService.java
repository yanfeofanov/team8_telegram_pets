package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.exception.ShelterNotFoundException;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.ShelterRepository;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Shelter findByType(TypeOfPet typeOfPet) {
        return shelterRepository.findByType(typeOfPet);
    }

    public Shelter addShelter(Shelter newShelter) {
        return shelterRepository.save(newShelter);
    }

    public void deleteShelterById(int id) {
        shelterRepository.deleteById(id);
    }

    public Shelter updateShelter(Shelter shelter) {
        if (shelterRepository.existsById(shelter.getId())) {
            return shelterRepository.save(shelter);
        } else {
         throw new ShelterNotFoundException("приют с id: " + shelter.getId() + " не найден!");
        }
    }
}
