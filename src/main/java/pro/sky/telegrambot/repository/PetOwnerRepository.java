package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.PetOwner;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Integer> {
    PetOwner findByUserIdAndProbationIsTrue(Long userId);
    PetOwner findPetOwnerByPhoneNumber(String phoneNumber);

    PetOwner findPetOwnerById(int petOwnerId);

}

