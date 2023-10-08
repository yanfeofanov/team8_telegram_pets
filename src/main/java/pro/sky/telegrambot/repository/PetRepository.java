package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.PetOwner;

import java.util.Collection;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    Collection<Pet> findAllByType(String typeOfPet);

    Collection<Pet> findAllByPetOwnerId(int petOwnerId);

    Collection<Pet> findAllByLeaveFalseAndPetOwnerIsNull();

    Collection<Pet> findAllByLeaveTrue();

    Collection<Pet> findAllByLeaveFalseAndPetOwnerIsNotNull();

    Collection<Pet> findAllByTypeAndLeaveIsFalseAndPetOwnerIsNull(String typeOfPet);

    Collection<Pet> findAllByTypeAndLeaveIsFalseAndPetOwnerIsNotNull(String typeOfPet);

    Pet getFirstByPetOwnerIdAndLeaveIsFalse(int petOwnerId);

    @Query(value = "select * from pet where type = 'CAT'", nativeQuery = true)
    Collection<Pet> findAllByTypeCat();
    @Query(value = "select * from pet where type = 'DOG'", nativeQuery = true)
    Collection<Pet> findAllByTypeDog();
}


