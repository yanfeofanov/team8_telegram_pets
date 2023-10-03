package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Volunteer;

import java.util.List;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
 List<Volunteer> findAll();
 Volunteer findVolunteerByPhoneNumber(String phoneNumber);

}
