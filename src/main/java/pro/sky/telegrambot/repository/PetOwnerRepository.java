package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.PetOwner;

import java.time.LocalDateTime;
import java.util.Collection;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Integer> {
    PetOwner findByUserIdAndProbationIsTrue(Long userId);

    PetOwner findPetOwnerByPhoneNumber(String phoneNumber);

    PetOwner findPetOwnerById(int petOwnerId);

    @Query(value = "select pet_owner.*, count(daily_report.id)\n" +
            "from pet_owner\n" +
            "         left join daily_report on pet_owner.id = daily_report.pet_owner_id\n" +
            "where  probation" + "\n" +
            // and date between :startPeriod and :endPeriod\n" +
            "group by pet_owner.id\n" +
            "having count(daily_report.id) = 0"
            , nativeQuery = true)
    Collection<PetOwner> getPetOwnersWhoDidNotSendReportForPeriod(LocalDateTime of, LocalDateTime of1);
}

