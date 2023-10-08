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



    @Query(value = "select pet_owner.*\n" +
            "from pet_owner\n" +
            "         left join daily_report on pet_owner.id = daily_report.pet_owner_id\n" +
            "where probation " +
            "and date between :startPeriod and :endPeriod\n" +
            "group by pet_owner.id\n" +
            "having count(daily_report.id) = 0"
            , nativeQuery = true)
    Collection<PetOwner> getPetOwnersWhoDidNotSendReportForPeriod(@Param("startPeriod") LocalDateTime startPeriod, @Param("endPeriod") LocalDateTime endPeriod);

    @Query(value = "select pet_owner.*\n" +
            "from pet_owner\n" +
            "         left join daily_report on pet_owner.id = daily_report.pet_owner_id\n" +
            "where probation " +
            "and date between :startPeriod and :endPeriod " +
            "and checked " +
            "and approved = false"
            , nativeQuery = true)
    Collection<PetOwner> getPetOwnersWhoSendBadReportForYesterday(@Param("startPeriod") LocalDateTime startPeriod, @Param("endPeriod") LocalDateTime endPeriod);
}

