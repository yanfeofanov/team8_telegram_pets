package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Service
public class TelegramBotService {

    private final UserRepository userRepository;
    private final InfoRepository infoRepository;
    private final ShelterRepository shelterRepository;
    private final KeyboardService keyboardService;
    private final TelegramBot telegramBot;

    public TelegramBotService(UserRepository userRepository, InfoRepository infoRepository, ShelterRepository shelterRepository, KeyboardService keyboardService, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.infoRepository = infoRepository;
        this.shelterRepository = shelterRepository;
        this.keyboardService = keyboardService;
        this.telegramBot = telegramBot;
    }

    public byte processMessage(Message message) {
        Long userId = message.from().id();
        Long chatId = message.chat().id();
        String commandStr = message.text();
        if (Commands.START.getCommand().equals(commandStr)) {
            if (isTheUserNew(userId)) {
                User newUser = addNewUser(userId, message.from().username(), message.from().firstName(), chatId);
                sendReply(chatId, generateGreetingText(newUser.getUserName()), null);
            }
            sendShelterSelectionMenu(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(commandStr)) {
            sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(commandStr)) {
            sendDogShelterMenu(chatId);
        }
        return 0;
    }

    public byte processCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String callbackCommand = callbackQuery.data();
        if (Commands.START.getCommand().equals(callbackCommand)) {
            sendShelterSelectionMenu(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(callbackCommand)) {
            sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(callbackCommand)) {
            sendDogShelterMenu(chatId);
        }
        return 0;
    }

    private void sendDogShelterMenu(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            return;
        }
        String aboutDogShelterStr = "";
        Info aboutDogShelterInfo = infoRepository.findByTypeAndShelter(TypesOfInformation.SHORT_INFO_ABOUT_SHELTER, shelter);
        if (aboutDogShelterInfo != null) {
            aboutDogShelterStr += aboutDogShelterInfo.getText();
        }
        sendReply(chatId, aboutDogShelterStr, generateDogShelterMenu());
    }

    private Keyboard generateDogShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_DOG_SHELTER,
                Commands.DOG_SHELTER_CONTACT_INFO,
                Commands.DOG_SHELTER_PASS_REG,
                Commands.SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER);
        return keyboardService.prepareInlineKeyboard(commandList);
    }

    private void sendCatShelterMenu(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            return;
        }
        String aboutCatShelterStr = "";
        Info aboutCatShelterInfo = infoRepository.findByTypeAndShelter(TypesOfInformation.SHORT_INFO_ABOUT_SHELTER, shelter);
        if (aboutCatShelterInfo != null) {
            aboutCatShelterStr += aboutCatShelterInfo.getText();
        }
        sendReply(chatId, aboutCatShelterStr, generateCatShelterMenu());
    }

    private Keyboard generateCatShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_CAT_SHELTER,
                Commands.CAT_SHELTER_CONTACT_INFO,
                Commands.CAT_SHELTER_PASS_REG,
                Commands.SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER);
        return keyboardService.prepareInlineKeyboard(commandList);
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

    public User addNewUser(Long id, String nickName, String name, Long chatId) {
        if (nickName == null || nickName.isEmpty()) {
            if (name == null || name.isEmpty()) {
                nickName = "дорогой гость";
            } else nickName = name;
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
