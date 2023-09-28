package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * метод асуществляет добовление нового пользователя в БД
     *
     * @param id       уникальный идентификатор пользователя в telegram
     * @param nickName логин пользователя
     * @param name     имя пользователя
     * @param chatId   уникальный идентификатор чата с пользователем в telegram
     * @return объект User содержащий информацию о пользователе бота в telegram
     */
    public User addNewUser(Long id, String nickName, String name, Long chatId) {
        if (nickName == null || nickName.isEmpty()) {
            if (name == null || name.isEmpty()) {
                nickName = "дорогой гость";
            } else {
                nickName = name;
            }
        }
        return userRepository.save(new User(id, nickName, chatId, LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId())));
    }

    public Boolean isTheUserNew(Long id) {
        return userRepository.findUserById(id) == null;
    }

    public User findUserById(Long userId) {
       return userRepository.findUserById(userId);
    }
}
