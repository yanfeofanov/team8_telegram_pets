package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;

import java.util.Collection;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    Info findByType(TypesOfInformation type);

    Info findByTypeAndShelter(TypesOfInformation type, Shelter shelter);

    Collection<Info> findByShelterId(int shelterId);


}
