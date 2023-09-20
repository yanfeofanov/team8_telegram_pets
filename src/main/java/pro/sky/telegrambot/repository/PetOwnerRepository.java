package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.PetOwner;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    PetOwner findPetOwnerById(long id);

    PetOwner findPetOwnerByChatId(long id);
}

