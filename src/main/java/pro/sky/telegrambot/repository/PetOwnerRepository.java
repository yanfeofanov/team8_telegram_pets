package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.PetOwner;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Integer> {
    PetOwner findByUserIdAndProbationIsTrue(Long userId);

}

