package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Pet;

import java.util.Collection;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    Pet findById(int id);

    Collection<Pet> findAllByType(String typeOfPet);

    Collection<Pet> findAllByPetOwnerId(int petOwnerId);

    Collection<Pet> findAllByLeaveFalseAndPetOwnerIsNull();

    Collection<Pet> findAllByLeaveTrue();

    Collection<Pet> findAllByLeaveFalseAndPetOwnerIsNotNull();

    Collection<Pet> findAllByTypeAndLeaveIsFalseAndPetOwnerIsNull(String typeOfPet);

    Collection<Pet> findAllByTypeAndLeaveIsFalseAndPetOwnerIsNotNull(String typeOfPet);

    Pet getFirstByPetOwnerIdAndLeaveIsFalse(int petOwnerId);
}


