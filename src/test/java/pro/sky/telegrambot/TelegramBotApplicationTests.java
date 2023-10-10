package pro.sky.telegrambot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.telegrambot.controller.PetController;
import pro.sky.telegrambot.service.TelegramBotService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotApplicationTests {

    @Autowired
    private PetController petController;
    @Autowired
    private TelegramBotService telegramBotService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(petController);
        Assertions.assertNotNull(telegramBotService);
    }
}
