package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface CommunicationRequestRepository extends JpaRepository<CommunicationRequest, Integer> {
    CommunicationRequest findByUserAndDoneIsFalse(User user);

    Collection<CommunicationRequest> findByDateBetweenAndDoneIsTrue(LocalDateTime startPeriod, LocalDateTime endPeriod);

    Collection<CommunicationRequest> findByDateBetweenAndDoneIsFalse(LocalDateTime startPeriod, LocalDateTime endPeriod);
}
