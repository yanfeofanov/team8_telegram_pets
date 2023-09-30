package pro.sky.telegrambot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import pro.sky.telegrambot.controller.PetController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TelegramBotApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private PetController petController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(petController);
    }
}
