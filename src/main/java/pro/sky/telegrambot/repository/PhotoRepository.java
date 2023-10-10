package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}

