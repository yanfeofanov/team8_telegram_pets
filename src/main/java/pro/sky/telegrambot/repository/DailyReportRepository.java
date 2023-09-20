package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.DailyReport;
import pro.sky.telegrambot.model.PetOwner;

import java.time.LocalDateTime;
import java.util.Collection;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    DailyReport findDailyReportById(long id);

    Collection<DailyReport> findDailyReportByPetOwner(PetOwner petOwner);

    Collection<DailyReport> findDailyReportByDateBetween(LocalDateTime begin, LocalDateTime end);
}

