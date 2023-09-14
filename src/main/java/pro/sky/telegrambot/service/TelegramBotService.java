package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
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
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.repository.VolunteerRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * класс-сервис содержащий методы по обработке всех команд телеграмм бота
 */
@Service
public class TelegramBotService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final UserRepository userRepository;
    private final InfoRepository infoRepository;
    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;
    private final KeyboardService keyboardService;
    private final TelegramBot telegramBot;

    public TelegramBotService(UserRepository userRepository, InfoRepository infoRepository, ShelterRepository shelterRepository, VolunteerRepository volunteerRepository, KeyboardService keyboardService, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.infoRepository = infoRepository;
        this.shelterRepository = shelterRepository;
        this.volunteerRepository = volunteerRepository;
        this.keyboardService = keyboardService;
        this.telegramBot = telegramBot;
    }

    /**
     * метод осуществляет обработку сообщений от пользователя в чате телеграм бота
     * @param message объект содержащий текст сообщения от пользователя в чат боте
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
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
        } else if (Commands.CAT_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            sendCatShelterContactInfo(chatId);
        } else if (Commands.CAT_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            sendCatShelterPassReg(chatId);
        } else if (Commands.SHELTER_SAFETY_RECOMMENDATIONS.getCommand().equals(commandStr)) {
            sendShelterSafetyRecommendations(chatId);
        } else if (Commands.COMMUNICATION_REQUEST.getCommand().equals(commandStr)) {
            sendCommunicationRequest(chatId);
        } else if (Commands.DOG_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            sendDogShelterContactInfo(chatId);
        } else if (Commands.DOG_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            sendDogShelterPassReg(chatId);
        } else if (Commands.ABOUT_DOG_SHELTER.getCommand().equals(commandStr)) {
            sendInfoAboutDogShelter(chatId);
        } else if (Commands.ADOPT_CAT.getCommand().equals(commandStr)) {
            sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.ADOPT_DOG.getCommand().equals(commandStr)) {
            sendMenuPreparingForAdoptionDog(chatId);
        } else if (Commands.CALL_VOLUNTEER.getCommand().equals(commandStr)) {
            callVolunteer(userId, chatId);
        }

        return 0;
    }

    /**
     * метод осуществляет обработку ответов пользователя на сообщения от бота, в том числе нажатие кнопок меню
     * @param callbackQuery объект содержащий текст ответа пользователя а также исходное сообщение
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
    public byte processCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String callbackCommand = callbackQuery.data();
        if (Commands.START.getCommand().equals(callbackCommand)) {
            sendShelterSelectionMenu(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(callbackCommand)) {
            sendCatShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(callbackCommand)) {
            sendDogShelterMenu(chatId);
        } else if (Commands.ADOPT_CAT.getCommand().equals(callbackCommand)) {
            sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.ADOPT_DOG.getCommand().equals(callbackCommand)) {
            sendMenuPreparingForAdoptionDog(chatId);
        } else if (Commands.ABOUT_CAT_SHELTER.getCommand().equals(callbackCommand)) {
            sendInfoAboutCatShelter(chatId);
        } else if (Commands.CAT_SHELTER_CONTACT_INFO.getCommand().equals(callbackCommand)) {
            sendCatShelterContactInfo(chatId);
        } else if (Commands.CAT_SHELTER_PASS_REG.getCommand().equals(callbackCommand)) {
            sendCatShelterPassReg(chatId);
        } else if (Commands.SHELTER_SAFETY_RECOMMENDATIONS.getCommand().equals(callbackCommand)) {
            sendShelterSafetyRecommendations(chatId);
        } else if (Commands.ABOUT_DOG_SHELTER.getCommand().equals(callbackCommand)) {
            sendInfoAboutDogShelter(chatId);

        } else if (Commands.DOG_SHELTER_CONTACT_INFO.getCommand().equals(callbackCommand)) {
            sendDogShelterContactInfo(chatId);
        } else if (Commands.DOG_SHELTER_PASS_REG.getCommand().equals(callbackCommand)) {
            sendDogShelterPassReg(chatId);
        } else if (Commands.CALL_VOLUNTEER.getCommand().equals(callbackCommand)) {
            callVolunteer(callbackQuery.from().id(), chatId);
        }
        return 0;
    }
    private void sendCatShelterContactInfo(Long chatId) {
       String textAboveMenu = "График работы: с 8:00 до 20:00, воскресенье выходной, Aдрес проезда: Туполева 12А главные ворота";
       sendReply(chatId,textAboveMenu, keyboardService.generateCatShelterMenu());
    }
    private void sendCatShelterPassReg(Long chatId) {
        String textAboveMenu = "Для оформления пропуска на территорию приюта для кошек, необходимо сообщить марку и госномер автомобиля";
        sendReply(chatId,textAboveMenu, keyboardService.generateCatShelterMenu());
    }
    private void sendShelterSafetyRecommendations(Long chatId) {
        String textAboveMenu = " На территориию приюта необходимо соблюдать правила безопасности при обращении с животными";
        sendReply(chatId,textAboveMenu, keyboardService.generateCatShelterMenu());
    }

    private void sendCommunicationRequest(Long chatId) {
        String textAboveMenu = " На территориию приюта необходимо соблюдать правила безлпасности при обращении с животными";
        sendReply(chatId,textAboveMenu, keyboardService.generateCatShelterMenu());
    }
    private void sendMenuPreparingForAdoptionCat(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.CAT));
    }
    private void sendMenuPreparingForAdoptionDog(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.DOG));
    }

    private void sendDogShelterContactInfo(Long chatId) {
        String textAboveMenu = "График работы: с 8:30 до 22, воскресенье выходной, Aдрес приюта: Проезд Тупиковый 2А, вход с торца ";
        sendReply(chatId,textAboveMenu, keyboardService.generateDogShelterMenu());
    }
    private void sendDogShelterPassReg(Long chatId) {
        String textAboveMenu = "Для оформления пропуска на территорию приюта для собак, необходимо сообщить марку машины и госномер автомобиля";
        sendReply(chatId, textAboveMenu, keyboardService.generateDogShelterMenu());
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

    /**
     * метод получает весь список имеющихся команд бота с описаниями, из перечисления Commands
     * и выводит их в чат пользователю
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
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
            aboutDogShelterStr += "_" + aboutDogShelterInfo.getText() + "_";
        }
        sendReply(chatId, aboutDogShelterStr, keyboardService.generateDogShelterMenu());
    }

    private void sendCatShelterMenu(Long chatId) {
        Shelter shelter = shelterRepository.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            return;
        }
        String aboutCatShelterStr = "";
        Info aboutCatShelterInfo = infoRepository.findByTypeAndShelter(TypesOfInformation.SHORT_INFO_ABOUT_SHELTER, shelter);
        if (aboutCatShelterInfo != null) {
            aboutCatShelterStr += "_" + aboutCatShelterInfo.getText() + "_";
        }
        sendReply(chatId, aboutCatShelterStr, keyboardService.generateCatShelterMenu());
    }

    private void sendShelterSelectionMenu(Long chatId) {
        sendReply(chatId, "_выберите какой приют вас интересует_", keyboardService.generateShelterSelectionMenu());
    }

    private String generateGreetingText(String nickName) {
        String greeting = "";
        greeting += nickName;
        Info aboutBot = infoRepository.findByType(TypesOfInformation.INFO_ABOUT_BOT);
        if (aboutBot != null) {
            greeting = greeting + " вас приветсвует " + aboutBot.getText();
        }
        return greeting;
    }

    private Boolean isTheUserNew(Long id) {
        return userRepository.findUserById(id) == null;
    }

    /**
     * метод асуществляет добовление нового пользователя в БД
     * @param id уникальный идентификатор пользователя в telegram
     * @param nickName логин пользователя
     * @param name имя пользователя
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     * @return объект User содержащий информацию о пользователе бота в telegram
     */
    private User addNewUser(Long id, String nickName, String name, Long chatId) {
        if (nickName == null || nickName.isEmpty()) {
            if (name == null || name.isEmpty()) {
                nickName = "дорогой гость";
            } else nickName = name;
        }
        return userRepository.save(new User(id, nickName, chatId, LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId())));
    }

    private SendResponse sendReply(Long chatId, String text, Keyboard keyboard) {
        SendMessage message = new SendMessage(chatId, text);
        message.parseMode(ParseMode.Markdown);
        if (keyboard != null) {
            message.replyMarkup(keyboard);
        }
        return telegramBot.execute(message);
    }

    /**
     * метод осуществляет поиск в бд данных о волонтерах, выбирает случайного из них в случае наличия.
     * Отправляет волонтеру ссылку на профиль пользователя запросившего связь с волонтером, и информирует
     * пользователя о том что волонтер получил запрос
     * @param userId уникальный идентификатор пользователя в telegram
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
    private void callVolunteer(Long userId, Long chatId) {
        Volunteer volunteer = chooseVolunteer();
        if (volunteer == null) {
            sendReply(chatId, "к сожалению в данный момент нет возможности пригласить волонтера", null);
            return;
        }
        Long volunteerChatId = volunteer.getUser().getChatId();
        String button_url = "tg://user?id=" + userId;
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineButton = new InlineKeyboardButton("пользователь");
        inlineButton.url(button_url);
        keyboard.addRow(inlineButton);
        SendResponse sendResponse = sendReply(volunteerChatId, "_поступил запрос на вызов волонтера от пользователя_", keyboard);
        if (sendResponse.isOk()) {
            sendReply(chatId, "запрос отправлен волонтеру, он напишет вам как только освободится", null);
        }
    }

    private Volunteer chooseVolunteer() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        if (volunteers.isEmpty()) {
            return null;
        }
        Collections.shuffle(volunteers);
        return volunteers.get(0);
    }
}
