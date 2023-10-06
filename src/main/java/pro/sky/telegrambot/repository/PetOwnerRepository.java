package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambot.model.PetOwner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Integer> {
    PetOwner findByUserIdAndProbationIsTrue(Long userId);
    PetOwner findPetOwnerByPhoneNumber(String phoneNumber);
    PetOwner findPetOwnerById(int petOwnerId);
    Collection<PetOwner> findAllByProbationIsTrue();
    @Query(value = "select * from PetOwner where DATE(end_probation) = :data", nativeQuery = true)
    List<PetOwner> findAllByEndProbationIsToday(@Param("data") LocalDate localDate);



}

