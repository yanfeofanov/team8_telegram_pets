package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findById(int id);

    Pet findByPetOwnerIdAndLeaveFalse(int petOwnerId);
}

