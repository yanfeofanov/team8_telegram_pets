package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * класс дает возможность получить списком всех пользователей, заинтересовавшихся нашим приютом
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public Collection<User> getAllUsers() {
        return new ArrayList<>();
    }

}
