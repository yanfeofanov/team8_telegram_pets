package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.repository.PetOwnerRepository;

@Service
public class PetOwnerService {
        private final PetOwnerRepository petOwnerRepository;

    public PetOwnerService(PetOwnerRepository petOwnerRepository) {
        this.petOwnerRepository = petOwnerRepository;
    }

    public PetOwner findPetOwnerWithProbationaryPeriod(Long userId) {
        return petOwnerRepository.findByUserIdAndProbationIsTrue(userId);
    }
}
