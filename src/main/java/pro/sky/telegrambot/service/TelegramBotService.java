package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class TelegramBotService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
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
        } else if (Commands.HELP.getCommand().equals(commandStr)) {
            sendHelpInformation(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(commandStr)) {
            sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(commandStr)) {
            sendDogShelterMenu(chatId);
        } else if (Commands.ABOUT_CAT_SHELTER.getCommand().equals(commandStr)) {
            sendInfoAboutCatShelter(chatId);
        }else if (Commands.ABOUT_DOG_SHELTER.getCommand().equals(commandStr)) {
            sendInfoAboutDogShelter(chatId);
        } else if (Commands.ADOPT_CAT.getCommand().equals(commandStr)) {
            sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.ADOPT_DOG.getCommand().equals(commandStr)) {
            sendMenuPreparingForAdoptionDog(chatId);
        }
        return 0;
    }

    public byte processCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String callbackCommand = callbackQuery.data();
        if (Commands.CAT_SHELTER.getCommand().equals(callbackCommand)) {
            sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(callbackCommand)) {
            sendDogShelterMenu(chatId);
        } else if (Commands.ADOPT_CAT.getCommand().equals(callbackCommand)) {
            sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.ADOPT_DOG.getCommand().equals(callbackCommand)) {
            sendMenuPreparingForAdoptionDog(chatId);
        } else if (Commands.ABOUT_CAT_SHELTER.getCommand().equals(callbackCommand)) {
            sendInfoAboutCatShelter(chatId);
        } else if (Commands.ABOUT_DOG_SHELTER.getCommand().equals(callbackCommand)) {
            sendInfoAboutDogShelter(chatId);
        }
        return 0;
    }

    private void sendMenuPreparingForAdoptionCat(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        sendReply(chatId, textAboveMenu, generateMenuPreparingForAdoption(TypeOfPet.CAT));
    }

    private void sendMenuPreparingForAdoptionDog(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        sendReply(chatId, textAboveMenu, generateMenuPreparingForAdoption(TypeOfPet.DOG));
    }

    private Keyboard generateMenuPreparingForAdoption(TypeOfPet typeOfPet) {
        List<Commands> commandList = new ArrayList<>();
        commandList.add(Commands.RULES_FOR_GETTING_TO_KNOW_PET);
        commandList.add(Commands.DOCUMENTS_FOR_ADOPTION);
        commandList.add(Commands.TRANSPORTATION_RECOMMENDATION);
        commandList.add(Commands.RECOMMENDATION_FOR_CUB_HOUSE);
        commandList.add(Commands.RECOMMENDATION_FOR_ADULT_PET_HOUSE);
        commandList.add(Commands.RECOMMENDATION_FOR_DISABLED_PET_HOUSE);
        commandList.add(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION);
        if (TypeOfPet.DOG.equals(typeOfPet)) {
            commandList.add(Commands.TIPS_FROM_DOG_HANDLER);
            commandList.add(Commands.RECOMMENDED_DOG_HANDLERS_LIST);
            commandList.add(Commands.COMMUNICATION_REQUEST);
            commandList.add(Commands.CALL_VOLUNTEER);
        }
        return keyboardService.prepareInlineKeyboard(commandList);
    }

    private void sendInfoAboutDogShelter(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return;
        }
        Info infoAboutDogShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutDogShelter != null) {
            sendReply(chatId, infoAboutDogShelter.getText(), null);
        } else {
            logger.info("no info about dog shelter");
        }
    }

    private void sendInfoAboutCatShelter(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return;
        }
        Info infoAboutCatShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutCatShelter != null) {
            sendReply(chatId, infoAboutCatShelter.getText(), null);
        } else {
            logger.info("no info about cat shelter");
        }
    }

    private void sendHelpInformation(Long chatId) {
        StringBuilder textMessage = new StringBuilder();
        for (Commands command : Commands.values()) {
            textMessage.append(command.getCommand()).append(" - ").append(command.getDescription()).append("\n");
        }
        sendReply(chatId, textMessage.toString(), null);
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
                Commands.ADOPT_DOG,
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
                Commands.ADOPT_CAT,
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
