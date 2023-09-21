package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.constant.TypeOfWaiting;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.ShelterRepository;

import java.util.*;
import java.util.regex.Pattern;

/**
 * класс-сервис содержащий методы по обработке всех команд телеграмм бота
 */
@Service
public class TelegramBotService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final Map<Long, TypeOfWaiting> chatsWaitingForInformation = new HashMap<>();
    private final Pattern patternPhoneNumber = Pattern.compile("89\\d{9}");
    private final Pattern patternEmail = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9\\.\\-_]*[A-Za-z0-9]*@([A-Za-z0-9]+([A-Za-z0-9-]*[A-Za-z0-9]+)*\\.)+[A-Za-z]*$");
    private final InfoRepository infoRepository;
    private final ShelterRepository shelterRepository;
    private final KeyboardService keyboardService;
    private final CommunicationRequestService communicationRequestService;
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final TelegramBot telegramBot;

    public TelegramBotService(InfoRepository infoRepository, ShelterRepository shelterRepository, KeyboardService keyboardService,
                              CommunicationRequestService communicationRequestService, UserService userService,
                              VolunteerService volunteerService, TelegramBot telegramBot) {
        this.infoRepository = infoRepository;
        this.shelterRepository = shelterRepository;
        this.keyboardService = keyboardService;
        this.communicationRequestService = communicationRequestService;
        this.userService = userService;
        this.volunteerService = volunteerService;
        this.telegramBot = telegramBot;
    }

    /**
     * метод осуществляет обработку сообщений от пользователя в чате телеграм бота
     *
     * @param message объект содержащий текст сообщения от пользователя в чат боте
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
    public int processMessage(Message message) {
        Long userId = message.from().id();
        Long chatId = message.chat().id();
        String textMassage = message.text();
        if (textMassage.length() > 1 && textMassage.startsWith("/")) {
            chatsWaitingForInformation.remove(chatId);
            if (Commands.START.getCommand().equals(textMassage) && userService.isTheUserNew(userId)) {
                User newUser = userService.addNewUser(userId, message.from().username(), message.from().firstName(), chatId);
                sendReply(chatId, generateGreetingText(newUser.getUserName()));
            }
            return processCommand(userId, chatId, textMassage);
        } else {
            return processInputOfInformation(userId, chatId, textMassage);
        }
    }

    /**
     * метод осуществляет обработку нажатий кнопок меню
     *
     * @param callbackQuery объект содержащий текст ответа пользователя а также исходное сообщение
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
    public int processCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        Long userId = callbackQuery.from().id();
        chatsWaitingForInformation.remove(chatId);
        String callbackCommand = callbackQuery.data();
        return processCommand(userId, chatId, callbackCommand);
    }

    public int processCommand(Long userId, Long chatId, String commandStr) {
        if (Commands.START.getCommand().equals(commandStr)) {
            return sendShelterSelectionMenu(chatId);
        } else if (Commands.HELP.getCommand().equals(commandStr)) {
            return sendHelpInformation(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(commandStr)) {
            return sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(commandStr)) {
            return sendDogShelterMenu(chatId);
        } else if (Commands.ADOPT_CAT.getCommand().equals(commandStr)) {
            return sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.ADOPT_DOG.getCommand().equals(commandStr)) {
            return sendMenuPreparingForAdoptionDog(chatId);
        } else if (Commands.ABOUT_CAT_SHELTER.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelter(chatId);
        } else if (Commands.ABOUT_DOG_SHELTER.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelter(chatId);
        } else if (Commands.CALL_VOLUNTEER.getCommand().equals(commandStr)) {
            return callVolunteer(userId, chatId);
        } else if (Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterSafetyRecommendation(chatId);
        } else if (Commands.COMMUNICATION_REQUEST.getCommand().equals(commandStr)) {
            return sendCommunicationOptionMenu(chatId);
        } else if (Commands.PHONE.getCommand().equals(commandStr)) {
            return sendRequestToEnterPhoneNumber(chatId);
        } else if (Commands.EMAIL.getCommand().equals(commandStr)) {
            return sendRequestToEnterEmail(chatId);
        } else if (Commands.BACK_DOG_SHELTER.getCommand().equals(commandStr)) {
            return sendDogShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterContact(chatId);
        } else if (Commands.DOG_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterPassRegInfo(chatId);
        } else if (Commands.BACK_CAT_SHELTER.getCommand().equals(commandStr)) {
            return sendCatShelterMenu(chatId);
        } else if (Commands.CAT_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterPassRegInfo(chatId);
        } else if (Commands.CAT_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterContact(chatId);
        } else if (Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterSafetyRecommendation(chatId);
        }
        return 0;
    }

    private int processInputOfInformation(Long userId, Long chatId, String textMassage) {
        TypeOfWaiting typeOfWaiting = chatsWaitingForInformation.get(chatId);
        if (typeOfWaiting == null) {
            return sendReply(chatId, "введенная вами команда не распознана ботом").errorCode();
        } else if (typeOfWaiting == TypeOfWaiting.PHONE_NUMBER) {
            if (validatePhoneNumber(textMassage)) {
                return createCommunicationRequest(userId, chatId, textMassage);
            } else {
                return sendReply(chatId, "Телефонный номер введен не корректно! Повторите пожалуйста ввод.").errorCode();
            }
        } else if (typeOfWaiting == TypeOfWaiting.EMAIL) {
            if (validateEmail(textMassage)) {
                return createCommunicationRequest(userId, chatId, textMassage);
            } else {
                return sendReply(chatId, "Адрес электронной почты введен не корректно! Повторите пожалуйста ввод.").errorCode();
            }
        }
        return sendReply(chatId, "введенная вами команда не распознана ботом").errorCode();
    }

    private int createCommunicationRequest(Long userId, Long chatId, String contactInfo) {
        CommunicationRequest communicationRequest = communicationRequestService.addRequestToDatabase(userId, contactInfo);
        if (communicationRequest != null) {
            chatsWaitingForInformation.remove(chatId);
            return sendReply(chatId, "Запрос на обратную связь успешно отправлен. Наши волонтеры свяжется с вами в ближайшее время.").errorCode();
        } else {
            return sendReply(chatId, "Запрос на обратную связь не создан!").errorCode();
        }
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return patternPhoneNumber.matcher(phoneNumber).matches();
    }

    private boolean validateEmail(String email) {
        return patternEmail.matcher(email).matches();
    }

    private int sendRequestToEnterPhoneNumber(Long chatId) {
        chatsWaitingForInformation.put(chatId, TypeOfWaiting.PHONE_NUMBER);
        return sendReply(chatId, "введите номер телефона для связи в формате - 89093568391").errorCode();
    }

    private int sendRequestToEnterEmail(Long chatId) {
        chatsWaitingForInformation.put(chatId, TypeOfWaiting.EMAIL);
        return sendReply(chatId, "введите адрес электронной почты для связи").errorCode();
    }

    private int sendCommunicationOptionMenu(Long chatId) {
        return sendReply(chatId, "выберите способ для связи:", keyboardService.generateCommunicationOptionMenu()).errorCode();
    }

    private int sendMenuPreparingForAdoptionCat(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        return sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.CAT)).errorCode();
    }

    private int sendMenuPreparingForAdoptionDog(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        return sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.DOG)).errorCode();
    }


    private int sendInfoAboutDogShelter(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return -1;
        }
        Info infoAboutDogShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutDogShelter != null) {
            return sendReply(chatId, infoAboutDogShelter.getText(), null).errorCode();
        } else {
            logger.info("no info about dog shelter");
            return -1;
        }
    }

    private int sendInfoAboutDogShelterContact(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return -1;
        }
        Info infoAboutDogShelterContact = infoRepository.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelter);
        if (infoAboutDogShelterContact != null) {
            return sendReply(chatId, infoAboutDogShelterContact.getText(), keyboardService.backButtonMenuDog()).errorCode();
        } else {
            logger.info("no info about dog shelter");
            return -1;
        }
    }

    private int sendInfoAboutDogShelterSafetyRecommendation(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return -1;
        }
        Info infoAboutDogShelterRecommendation = infoRepository.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelter);
        if (infoAboutDogShelterRecommendation != null) {
            return sendReply(chatId, infoAboutDogShelterRecommendation.getText(), keyboardService.backButtonMenuDog()).errorCode();
        } else {
            logger.info("no info about dog shelter");
            return -1;
        }

    }

    private int sendInfoAboutDogShelterPassRegInfo(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return -1;
        }
        Info infoAboutDogShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelter);
        if (infoAboutDogShelter != null) {
            return sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backButtonMenuDog()).errorCode();
        } else {
            logger.info("no info about dog shelter");
            return -1;
        }

    }

    private int sendInfoAboutCatShelterPassRegInfo(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return -1;
        }
        Info infoAboutCatShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelter);
        if (infoAboutCatShelter != null) {
            return sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backButtonMenuCat()).errorCode();
        } else {
            logger.info("no info about cat shelter");
            return -1;
        }
    }

    private int sendInfoAboutCatShelterContact(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return -1;
        }
        Info infoAboutCatShelterContact = infoRepository.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelter);
        if (infoAboutCatShelterContact != null) {
            return sendReply(chatId, infoAboutCatShelterContact.getText(), keyboardService.backButtonMenuCat()).errorCode();
        } else {
            logger.info("no info about cat shelter");
            return -1;
        }
    }

    private int sendInfoAboutCatShelterSafetyRecommendation(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return -1;
        }
        Info infoAboutCatShelterSafetyRecommendation = infoRepository.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelter);
        if (infoAboutCatShelterSafetyRecommendation != null) {
            return sendReply(chatId, infoAboutCatShelterSafetyRecommendation.getText(), keyboardService.backButtonMenuCat()).errorCode();
        } else {
            logger.info("no info about cat shelter");
            return -1;
        }

    }

    private int sendInfoAboutCatShelter(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return -1;
        }
        Info infoAboutCatShelter = infoRepository.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutCatShelter != null) {
            return sendReply(chatId, infoAboutCatShelter.getText(), null).errorCode();
        } else {
            logger.info("no info about cat shelter");
            return -1;
        }
    }

    /**
     * метод получает весь список имеющихся команд бота с описаниями, из перечисления Commands
     * и выводит их в чат пользователю
     *
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
    private int sendHelpInformation(Long chatId) {
        StringBuilder textMessage = new StringBuilder();
        for (Commands command : Commands.values()) {
            if (command.getCommand().startsWith("/")) {
                textMessage.append(command.getCommand()).append(" - ").append(command.getDescription()).append("\n");
            }
        }
        return sendReply(chatId, textMessage.toString(), null).errorCode();
    }

    private int sendDogShelterMenu(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            return -1;
        }
        String aboutDogShelterStr;
        Info aboutDogShelterInfo = infoRepository.findByTypeAndShelter(TypesOfInformation.SHORT_INFO_ABOUT_SHELTER, shelter);
        if (aboutDogShelterInfo != null) {
            aboutDogShelterStr = aboutDogShelterInfo.getText();
        } else {
            aboutDogShelterStr = "приют для собак";
        }
        return sendReply(chatId, aboutDogShelterStr, keyboardService.generateDogShelterMenu()).errorCode();
    }

    private int sendCatShelterMenu(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            return -1;
        }
        String aboutCatShelterStr;
        Info aboutCatShelterInfo = infoRepository.findByTypeAndShelter(TypesOfInformation.SHORT_INFO_ABOUT_SHELTER, shelter);
        if (aboutCatShelterInfo != null) {
            aboutCatShelterStr = aboutCatShelterInfo.getText();
        } else {
            aboutCatShelterStr = "приют для кошек";
        }
        return sendReply(chatId, aboutCatShelterStr, keyboardService.generateCatShelterMenu()).errorCode();
    }

    private int sendShelterSelectionMenu(Long chatId) {
        return sendReply(chatId, "выберите какой приют вас интересует", keyboardService.generateShelterSelectionMenu()).errorCode();
    }

    private String generateGreetingText(String nickName) {
        String greeting = "здравствуйте ";
        greeting += nickName;
        Info aboutBot = infoRepository.findByType(TypesOfInformation.INFO_ABOUT_BOT);
        if (aboutBot != null) {
            greeting = greeting + ", вас приветсвует " + aboutBot.getText();
        }
        return greeting;
    }

    public SendResponse sendReply(Long chatId, String text, Keyboard keyboard) {
        SendMessage message = new SendMessage(chatId, text);
        //message.parseMode(ParseMode.Markdown);
        message.replyMarkup(keyboard);
        return telegramBot.execute(message);
    }

    public SendResponse sendReply(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        //message.parseMode(ParseMode.Markdown);
        return telegramBot.execute(message);
    }

    /**
     * метод осуществляет поиск в бд данных о волонтерах, выбирает случайного из них в случае наличия.
     * Отправляет волонтеру ссылку на профиль пользователя запросившего связь с волонтером, и информирует
     * пользователя о том что волонтер получил запрос
     *
     * @param userId уникальный идентификатор пользователя в telegram
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
    private int callVolunteer(Long userId, Long chatId) {
        Volunteer volunteer = volunteerService.getRandomVolunteer();
        if (volunteer == null) {
            sendReply(chatId, "к сожалению в данный момент нет возможности пригласить волонтера", null);
            return -1;
        }
        Long volunteerChatId = volunteer.getUser().getChatId();
        String button_url = "tg://user?id=" + userId;
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineButton = new InlineKeyboardButton("пользователь");
        inlineButton.url(button_url);
        keyboard.addRow(inlineButton);
        SendResponse sendResponse = sendReply(volunteerChatId, "_поступил запрос на вызов волонтера от пользователя_", keyboard);
        if (sendResponse.isOk()) {
            sendReply(chatId, "запрос отправлен волонтеру, он напишет вам в личных сообщениях как только освободится", null);
        }
        return sendResponse.errorCode();
    }
}
