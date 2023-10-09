package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.*;
import pro.sky.telegrambot.model.*;

import java.io.IOException;
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
    private final InfoService infoService;
    private final ShelterService shelterService;
    private final KeyboardService keyboardService;
    private final CommunicationRequestService communicationRequestService;
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final PetOwnerService petOwnerService;
    private final DailyReportService dailyReportService;
    private final TelegramBot telegramBot;
    private int lastMessageId;
    private final Map<Long, Boolean> lastMessageIsReplaceableMenu = new HashMap<>();

    public TelegramBotService(InfoService infoService, ShelterService shelterService, KeyboardService keyboardService,
                              CommunicationRequestService communicationRequestService, UserService userService,
                              VolunteerService volunteerService, PetOwnerService petOwnerService, DailyReportService dailyReportService, TelegramBot telegramBot) {
        this.infoService = infoService;
        this.shelterService = shelterService;
        this.keyboardService = keyboardService;
        this.communicationRequestService = communicationRequestService;
        this.userService = userService;
        this.volunteerService = volunteerService;
        this.petOwnerService = petOwnerService;
        this.dailyReportService = dailyReportService;
        this.telegramBot = telegramBot;
    }

    /**
     * метод осуществляет обработку сообщений от пользователя в чате телеграм бота
     *
     * @param message объект содержащий текст сообщения от пользователя в чат боте
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
    public BaseResponse processMessage(Message message) {
        Long userId = message.from().id();
        Long chatId = message.chat().id();
        String textMassage = message.text();
        PhotoSize[] photoSizes = message.photo();
        if (textMassage != null && textMassage.length() > 1 && textMassage.startsWith("/")) {
            chatsWaitingForInformation.remove(chatId);
            if (Commands.START.getCommand().equals(textMassage) && userService.isTheUserNew(userId)) {
                User newUser = userService.addNewUser(userId, message.from().username(), message.from().firstName(), chatId);
                sendReply(chatId, generateGreetingText(newUser.getUserName()));
            }
            return processCommand(userId, chatId, textMassage);
        } else if (textMassage != null || photoSizes != null) {
            return processInputOfInformation(userId, chatId, (photoSizes != null) ? message.caption().trim() : textMassage.trim(), photoSizes);
        } else {
            return sendReply(chatId, "извините, к сожалению наш бот не работает с данными такого типа");
        }
    }

    /**
     * метод осуществляет обработку нажатий кнопок меню
     *
     * @param callbackQuery объект содержащий текст ответа пользователя а также исходное сообщение
     * @return код ошибки обработки сообщения. 0 - означает отсутствие ошибок.
     */
    public BaseResponse processCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        Long userId = callbackQuery.from().id();
        chatsWaitingForInformation.remove(chatId);
        String callbackCommand = callbackQuery.data();
        return processCommand(userId, chatId, callbackCommand);
    }

    private BaseResponse processCommand(Long userId, Long chatId, String commandStr) {
        if (Commands.START.getCommand().equals(commandStr) || Commands.BACK_START_MENU.getCommand().equals(commandStr)) {
            return sendShelterSelectionMenu(chatId);
        } else if (Commands.HELP.getCommand().equals(commandStr)) {
            return sendHelpInformation(chatId);
        } else if (Commands.CAT_SHELTER.getCommand().equals(commandStr)) {
            return sendCatShelterMenu(chatId);
        } else if (Commands.CAT_SHELTER_INFO_MENU.getCommand().equals(commandStr)) {
            return sendCatInfoMenu(chatId);
        } else if (Commands.DOG_SHELTER.getCommand().equals(commandStr)) {
            return sendDogShelterMenu(chatId);
        } else if (Commands.DOG_SHELTER_INFO_MENU.getCommand().equals(commandStr)) {
            return sendDogInfoMenu(chatId);
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
        } else if (Commands.REPORT_ABOUT_PET.getCommand().equals(commandStr)) {
            return sendRequestToEnterDailyReport(userId, chatId);
        } else if (Commands.CAT_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterContact(chatId);
        } else if (Commands.DOG_SHELTER_CONTACT_INFO.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterContact(chatId);
        } else if (Commands.CAT_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterPassRegInfo(chatId);
        } else if (Commands.DOG_SHELTER_PASS_REG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterPassRegInfo(chatId);
        } else if (Commands.BACK_CAT_SHELTER.getCommand().equals(commandStr)) {
            return sendCatShelterMenu(chatId);
        } else if (Commands.BACK_DOG_SHELTER.getCommand().equals(commandStr)) {
            return sendDogShelterMenu(chatId);
        } else if (Commands.BACK_CAT_INFO_MENU.getCommand().equals(commandStr)) {
            return sendCatInfoMenu(chatId);
        } else if (Commands.BACK_DOG_INFO_MENU.getCommand().equals(commandStr)) {
            return sendDogInfoMenu(chatId);
        } else if (Commands.BACK_CAT_ADOPTION_MENU.getCommand().equals(commandStr)) {
            return sendMenuPreparingForAdoptionCat(chatId);
        } else if (Commands.BACK_DOG_ADOPTION_MENU.getCommand().equals(commandStr)) {
            return sendMenuPreparingForAdoptionDog(chatId);
        } else if (Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterSafetyRecommendation(chatId);
        } else if (Commands.RULES_FOR_GETTING_TO_KNOW_DOG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterGettingRules(chatId);
        } else if (Commands.RULES_FOR_GETTING_TO_KNOW_CAT.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterGettingRules(chatId);
        } else if (Commands.DOCUMENTS_FOR_ADOPTION_DOG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterDocumentAdoption(chatId);
        } else if (Commands.DOCUMENTS_FOR_ADOPTION_CAT.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterDocumentAdoption(chatId);
        } else if (Commands.TRANSPORTATION_RECOMMENDATION_FOR_DOG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterTransportationAnimal(chatId);
        } else if (Commands.TRANSPORTATION_RECOMMENDATION_FOR_CAT.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterTransportationAnimal(chatId);
        } else if (Commands.RECOMMENDATION_FOR_DOG_CUB_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterHouseCub(chatId);
        } else if (Commands.RECOMMENDATION_FOR_CAT_CUB_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterHouseCub(chatId);
        } else if (Commands.RECOMMENDATION_FOR_DOG_ADULT_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterHouseAdultPet(chatId);
        } else if (Commands.RECOMMENDATION_FOR_CAT_ADULT_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterHouseAdultPet(chatId);
        } else if (Commands.RECOMMENDATION_FOR_DISABLED_DOG_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterHouseDisablePet(chatId);
        } else if (Commands.RECOMMENDATION_FOR_DISABLED_CAT_HOUSE.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterHouseDisablePet(chatId);
        } else if (Commands.TIPS_FROM_DOG_HANDLER.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterTipsDogHandler(chatId);
        } else if (Commands.RECOMMENDED_DOG_HANDLERS_LIST.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterTipsDogHandlerList(chatId);
        } else if (Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_DOG.getCommand().equals(commandStr)) {
            return sendInfoAboutDogShelterPossibleReasonForRefusalAdoption(chatId);
        } else if (Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_CAT.getCommand().equals(commandStr)) {
            return sendInfoAboutCatShelterPossibleReasonForRefusalAdoption(chatId);
        }
        return null;
    }

    private BaseResponse processInputOfInformation(Long userId, Long chatId, String textMassage, PhotoSize[] photoSizes) {
        TypeOfWaiting typeOfWaiting = chatsWaitingForInformation.get(chatId);
        if (typeOfWaiting == null) {
            sendReply(chatId, "введенная вами команда не распознана ботом");
            return sendHelpInformation(chatId);
        } else if (typeOfWaiting == TypeOfWaiting.PHONE_NUMBER) {
            if (textMassage != null && validatePhoneNumber(textMassage)) {
                return createCommunicationRequest(userId, chatId, textMassage);
            } else {
                return sendReply(chatId, "Телефонный номер введен не корректно! Повторите пожалуйста ввод.");
            }
        } else if (typeOfWaiting == TypeOfWaiting.EMAIL) {
            if (textMassage != null && validateEmail(textMassage)) {
                return createCommunicationRequest(userId, chatId, textMassage);
            } else {
                return sendReply(chatId, "Адрес электронной почты введен не корректно! Повторите пожалуйста ввод.");
            }
        } else if (typeOfWaiting == TypeOfWaiting.DAILY_REPORT) {
            if (photoSizes != null && textMassage != null) {
                try {
                    if (dailyReportService.sendReport(userId, textMassage, photoSizes) != null) {
                        chatsWaitingForInformation.remove(chatId);
                        return sendReply(chatId, "отчет сохранен. Спасибо!");
                    } else sendReply(chatId, "Не удалось записать ваш отчет! Повторите пожалуйста снова.");
                } catch (IOException e) {
                    return sendReply(chatId, "Не удалось записать ваш отчет! Повторите пожалуйста снова.");
                }
            } else {
                return sendReply(chatId, "Данные для отчета введены в неверном формате!\n " +
                        "Отправьте отчет одним сообщением! Текст отчета должен быть размещен в описании под фото!");
            }
        }
        BaseResponse response = sendReply(chatId, "введенная вами команда не распознана ботом");
        if (response.isOk()) {
            return sendHelpInformation(chatId);
        } else {
            return null;
        }
    }

    private BaseResponse sendRequestToEnterDailyReport(Long userId, Long chatId) {
        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(userId);
        if (petOwner == null) {
            return sendReply(chatId, "вам не нужно отправлять отчет, т.к. вы не являетесь владельцем питомца на испытательном сроке.");
        }
        chatsWaitingForInformation.put(chatId, TypeOfWaiting.DAILY_REPORT);
        return sendReply(chatId, "пришлите пожалуйста отчет содержащий следующую информацию:\n" +
                "- фото животного\n" +
                "- рацион животного\n" +
                "- общее самочувствие и привыкание к новому месту\n" +
                "- изменение в поведении: отказ от старых привычек, приобретение новых\n" +
                "* Отправьте отчет одним сообщением! Текст отчета должен быть размещен в описании под фото!");
    }

    private BaseResponse createCommunicationRequest(Long userId, Long chatId, String contactInfo) {
        CommunicationRequest communicationRequest = communicationRequestService.addRequestToDatabase(userId, contactInfo);
        if (communicationRequest != null) {
            chatsWaitingForInformation.remove(chatId);
            return sendReply(chatId, "Запрос на обратную связь успешно отправлен. Наши волонтеры свяжется с вами в ближайшее время.");
        } else {
            return sendReply(chatId, "Запрос на обратную связь не создан!");
        }
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return patternPhoneNumber.matcher(phoneNumber).matches();
    }

    private boolean validateEmail(String email) {
        return patternEmail.matcher(email).matches();
    }

    private BaseResponse sendRequestToEnterPhoneNumber(Long chatId) {
        chatsWaitingForInformation.put(chatId, TypeOfWaiting.PHONE_NUMBER);
        return sendReply(chatId, "введите номер телефона для связи в формате - 89093568391");
    }

    private BaseResponse sendRequestToEnterEmail(Long chatId) {
        chatsWaitingForInformation.put(chatId, TypeOfWaiting.EMAIL);
        return sendReply(chatId, "введите адрес электронной почты для связи");
    }

    private BaseResponse sendCommunicationOptionMenu(Long chatId) {
        return sendReply(chatId, "выберите способ для связи:", keyboardService.generateCommunicationOptionMenu());
    }

    private BaseResponse sendMenuPreparingForAdoptionCat(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        return sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.CAT));
    }

    private BaseResponse sendMenuPreparingForAdoptionDog(Long chatId) {
        String textAboveMenu = "ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи";
        return sendReply(chatId, textAboveMenu, keyboardService.generateMenuPreparingForAdoption(TypeOfPet.DOG));
    }

    private BaseResponse sendInfoAboutDogShelter(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterGettingRules(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterDocumentAdoption(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterTransportationAnimal(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterHouseCub(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterHouseAdultPet(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterHouseDisablePet(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterTipsDogHandler(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.TIPS_FROM_DOG_HANDLER, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterTipsDogHandlerList(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.RECOMMENDED_DOG_HANDLERS, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterPossibleReasonForRefusalAdoption(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterContact(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelterContact = infoService.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelter);
        if (infoAboutDogShelterContact != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelterContact.getText(), keyboardService.backDogInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterSafetyRecommendation(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelterRecommendation = infoService.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelter);
        if (infoAboutDogShelterRecommendation != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelterRecommendation.getText(), keyboardService.backDogInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutDogShelterPassRegInfo(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            logger.error("no shelter with type \"dog\" found");
            return null;
        }
        Info infoAboutDogShelter = infoService.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelter);
        if (infoAboutDogShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutDogShelter.getText(), keyboardService.backDogInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about dog shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterPassRegInfo(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterContact(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelterContact = infoService.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelter);
        if (infoAboutCatShelterContact != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelterContact.getText(), keyboardService.backCatInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterSafetyRecommendation(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelterSafetyRecommendation = infoService.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelter);
        if (infoAboutCatShelterSafetyRecommendation != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelterSafetyRecommendation.getText(), keyboardService.backCatInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterGettingRules(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterDocumentAdoption(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterTransportationAnimal(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterHouseCub(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterHouseAdultPet(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterHouseDisablePet(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelterPossibleReasonForRefusalAdoption(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatAdoptionMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    private BaseResponse sendInfoAboutCatShelter(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            logger.error("no shelter with type \"cat\" found");
            return null;
        }
        Info infoAboutCatShelter = infoService.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter);
        if (infoAboutCatShelter != null) {
            BaseResponse response = sendReply(chatId, infoAboutCatShelter.getText(), keyboardService.backCatInfoMenu());
            lastMessageIsReplaceableMenu.put(chatId, true);
            return response;
        } else {
            logger.info("no info about cat shelter");
            return null;
        }
    }

    /**
     * метод получает весь список имеющихся команд бота с описаниями, из перечисления Commands
     * и выводит их в чат пользователю
     *
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
    private BaseResponse sendHelpInformation(Long chatId) {
        StringBuilder textMessage = new StringBuilder();
        for (Commands command : Commands.values()) {
            if (command.getCommand().startsWith("/")) {
                textMessage.append(command.getCommand()).append(" - ").append(command.getDescription()).append("\n");
            }
        }
        return sendReply(chatId, textMessage.toString());
    }

    private BaseResponse sendDogShelterMenu(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            return null;
        }
        String dogShelterStr = "приют для собак " + shelter.getName();
        BaseResponse response = sendReply(chatId, dogShelterStr, keyboardService.generateMainDogShelterMenu());
        lastMessageIsReplaceableMenu.put(chatId, true);
        return response;
    }

    private BaseResponse sendCatShelterMenu(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            return null;
        }
        String catShelterStr = "приют для кошек " + shelter.getName();
        BaseResponse response = sendReply(chatId, catShelterStr, keyboardService.generateMainCatShelterMenu());
        lastMessageIsReplaceableMenu.put(chatId, true);
        return response;
    }

    private BaseResponse sendCatInfoMenu(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.CAT);
        if (shelter == null) {
            return null;
        }
        String catShelterStr = "приют для кошек " + shelter.getName();
        BaseResponse response = sendReply(chatId, catShelterStr, keyboardService.generateInfoCatShelterMenu());
        lastMessageIsReplaceableMenu.put(chatId, true);
        return response;
    }

    private BaseResponse sendDogInfoMenu(Long chatId) {
        Shelter shelter = shelterService.findByType(TypeOfPet.DOG);
        if (shelter == null) {
            return null;
        }
        String dogShelterStr = "приют для собак " + shelter.getName();
        BaseResponse response = sendReply(chatId, dogShelterStr, keyboardService.generateInfoDogShelterMenu());
        lastMessageIsReplaceableMenu.put(chatId, true);
        return response;
    }

    private BaseResponse sendShelterSelectionMenu(Long chatId) {
        BaseResponse response = sendReply(chatId, "выберите какой приют вас интересует", keyboardService.generateShelterSelectionMenu());
        lastMessageIsReplaceableMenu.put(chatId, true);
        return response;
    }

    private String generateGreetingText(String nickName) {
        String greeting = "здравствуйте ";
        greeting += nickName;
        Info aboutBot = infoService.findByType(TypesOfInformation.INFO_ABOUT_BOT);
        if (aboutBot != null) {
            greeting = greeting + ", вас приветсвует " + aboutBot.getText();
        }
        return greeting;
    }

    /**
     * метод отправляет случайному волонтеру ссылку на профиль пользователя запросившего связь с волонтером, и информирует
     * пользователя о том что волонтер получил запрос
     *
     * @param userId уникальный идентификатор пользователя в telegram
     * @param chatId уникальный идентификатор чата с пользователем в telegram
     */
    private BaseResponse callVolunteer(Long userId, Long chatId) {
        Volunteer volunteer = volunteerService.getRandomVolunteer();
        if (volunteer == null || volunteer.getUser() == null) {
            return sendReply(chatId, "к сожалению в данный момент нет возможности пригласить волонтера");
        }
        BaseResponse response = sendReply(volunteer.getUser().getChatId(),
                "поступил запрос на вызов волонтера от пользователя", keyboardService.linkToBotUserButton(userId));
        if (response.isOk()) {
            response = sendReply(chatId, "запрос отправлен волонтеру,\n " +
                    "он напишет вам в личных сообщениях как только освободится");
        }
        return response;
    }

    public BaseResponse sendReply(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        if (lastMessageIsReplaceableMenu.get(chatId) != null && lastMessageIsReplaceableMenu.get(chatId)) {
            return editBotMassage(chatId, lastMessageId, text, keyboard);
        }
        SendMessage message = new SendMessage(chatId, text);
        //message.parseMode(ParseMode.Markdown);
        message.replyMarkup(keyboard);
        SendResponse response = telegramBot.execute(message);
        lastMessageId = response.message().messageId();
        return response;
    }

    public BaseResponse sendReply(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        //message.parseMode(ParseMode.MarkdownV2);
        SendResponse response = telegramBot.execute(message);
        lastMessageId = response.message().messageId();
        lastMessageIsReplaceableMenu.put(chatId, false);
        return response;
    }

    public BaseResponse editBotMassage(Long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText editMessageText = new EditMessageText(chatId, messageId, text);
        editMessageText.replyMarkup(keyboard);
        return telegramBot.execute(editMessageText);
    }

    @Scheduled(cron = "0 30 09 * * *")
    private void sendNotifications() {
        sendNotificationToOwnersAboutAbsenceOfReport();
        sendNotificationToVolunteerAboutAbsenceOfReport();
        sendNotificationToOwnersAboutBadReport();
    }

    private void sendNotificationToOwnersAboutAbsenceOfReport() {
        Collection<PetOwner> petOwners = petOwnerService.getPetOwnersWhoDidNotSendReportForYesterday();
        petOwners.forEach(petOwner -> sendReply(
                petOwner.getUser().getChatId(),
                "Здравствуйте!\n" +
                        " Вы вчера не отправили отчет о вашем питомце.\n" +
                        " Просим вас не забывать вовремя отправлять отчеты."));
    }

    private void sendNotificationToVolunteerAboutAbsenceOfReport() {
        Collection<PetOwner> petOwners = petOwnerService.getPetOwnersWhoDidNotSendReportForTwoDaysPlus();
        petOwners.forEach(
                petOwner -> {
                    Volunteer volunteer = petOwner.getVolunteer();
                    if (volunteer == null) {
                        volunteerService.getRandomVolunteer();
                    }
                    if (volunteer == null) {
                        return;
                    }
                    sendReply(
                            volunteer.getUser().getChatId(),
                            "Здравствуйте!\n" +
                                    " Свяжитесь пожалуйста с владельцем питомца,\n" +
                                    " он уже не менее 2х дней подряд не отправляет отчет.\n" +
                                    petOwner.getPhoneNumber() + " " + petOwner.getEmail(),
                            keyboardService.linkToBotUserButton(petOwner.getUser().getId()));
                }
        );
    }

    private void sendNotificationToOwnersAboutBadReport() {
        Collection<PetOwner> petOwners = petOwnerService.getPetOwnersWhoSendBadReportForYesterday();
        petOwners.forEach(petOwner -> {
                    Long chatId = petOwner.getUser().getChatId();
                    BaseResponse baseResponse = sendReply(
                            chatId,
                            "Здравствуйте!\n" +
                                    " Ваш вчерашний отчет о питомце не был принят нашим сотрудником.\n" +
                                    " Просим вас ознакомиться с правилами составления ежедневных отчетов и отнестись к этому более ответственно.");
                    if (baseResponse.isOk()) {
                        sendReply(
                                chatId,
                                "отчет должен содержать следующую информацию:\n" +
                                        "- фото животного\n" +
                                        "- рацион животного\n" +
                                        "- общее самочувствие и привыкание к новому месту\n" +
                                        "- изменение в поведении: отказ от старых привычек, приобретение новых");
                    }
                }
        );
    }
}
