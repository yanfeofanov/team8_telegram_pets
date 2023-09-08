package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
public class TelegramBotService {

    private final UserRepository userRepository;
    private final InfoRepository infoRepository;
    private final KeyboardService keyboardService;
    private final TelegramBot telegramBot;

    public TelegramBotService(UserRepository userRepository, InfoRepository infoRepository, KeyboardService keyboardService, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.infoRepository = infoRepository;
        this.keyboardService = keyboardService;
        this.telegramBot = telegramBot;
    }

    public int processMessage(Message message) {
        Long userId = message.from().id();
        Long chatId = message.chat().id();
        if ("/start".equals(message.text())) {
            if (isTheUserNew(userId)) {
                User newUser = addNewUser(userId, message.from().username(), chatId);
                sendReply(chatId, generateGreetingText(newUser.getUserName()), null);
            }
            sendShelterSelectionMenu(chatId);
        }
        return 0;
    }

    private void sendShelterSelectionMenu(Long chatId) {
        sendReply(chatId, "выберите какой приют вас интересует", generateShelterSelectionMenu());
    }

    private Keyboard generateShelterSelectionMenu() {
        List<Commands> commandList = List.of(Commands.CAT_SHELTER, Commands.DOG_SHELTER);
        return keyboardService.prepareInlineKeyboard(commandList);
    }

    private String generateGreetingText(String nickName) {
        String greeting = "";
        greeting += nickName;
        Info aboutBot = infoRepository.findByType(TypesOfInformation.INFO_ABOUT_BOT);
        if (aboutBot != null) {
            greeting = greeting + " " + aboutBot.getText();
        }
        return greeting;
    }

    private Boolean isTheUserNew(Long id) {
        return userRepository.findUserById(id) == null;
    }

    public User addNewUser(Long id, String nickName, Long chatId) {
        if (nickName == null || nickName.isEmpty()) {
            nickName = "дорогой гость";
        }
        return userRepository.save(new User(id, nickName, chatId, LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId())));
    }

    private SendResponse sendReply(Long chatId, String text, Keyboard keyboard) {
        SendMessage message = new SendMessage(chatId, text);
        if (keyboard != null) {
            message.replyMarkup(keyboard);
        }
        return telegramBot.execute(message);
    }
}
