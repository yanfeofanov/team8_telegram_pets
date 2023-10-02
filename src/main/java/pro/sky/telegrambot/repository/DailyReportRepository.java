package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.DailyReport;
import pro.sky.telegrambot.model.PetOwner;

import java.time.LocalDateTime;
import java.util.Collection;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    Collection<DailyReport> findDailyReportByChecked(boolean check);

    Collection<DailyReport> findDailyReportByPetOwner(PetOwner petOwner);

    DailyReport findByPetOwnerAndDateBetween(PetOwner petOwner, LocalDateTime begin, LocalDateTime end);

    Collection<DailyReport> findDailyReportByDateBetween(LocalDateTime begin, LocalDateTime end);

    DailyReport findDailyReportById(Long id);
}



