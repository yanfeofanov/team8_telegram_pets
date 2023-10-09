package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.model.Shelter;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotServiceTest {
    private final InfoService infoServiceMock = mock(InfoService.class);
    private final ShelterService shelterServiceMock = mock(ShelterService.class);
    private final KeyboardService keyboardServiceMock = mock(KeyboardService.class);
    private final CommunicationRequestService communicationRequestServiceMock = mock(CommunicationRequestService.class);
    private final UserService userServiceMock = mock(UserService.class);
    private final VolunteerService volunteerServiceMock = mock(VolunteerService.class);
    private final PetOwnerService petOwnerServiceMock = mock(PetOwnerService.class);
    private final DailyReportService dailyReportServiceMock = mock(DailyReportService.class);
    private final TelegramBot telegramBotMock = mock(TelegramBot.class);
    private final TelegramBotService out = new TelegramBotService(infoServiceMock, shelterServiceMock, keyboardServiceMock,
            communicationRequestServiceMock, userServiceMock, volunteerServiceMock,
            petOwnerServiceMock, dailyReportServiceMock, telegramBotMock);
    private final Message messageMock = mock(Message.class);
    private final CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
    private final User userMock = mock(User.class);
    private final Chat chatMock = mock(Chat.class);
    private final CommunicationRequest communicationRequestMock = mock(CommunicationRequest.class);
    private final SendResponse sendResponseMock = mock(SendResponse.class);
    private final PetOwner petOwnerMock = mock(PetOwner.class);
    private final Shelter shelterMock = mock(Shelter.class);
    private final Info infoMock = mock(Info.class);
    private final InlineKeyboardMarkup inlineKeyboardMarkupMock = mock(InlineKeyboardMarkup.class);
    static final pro.sky.telegrambot.model.User botUser = new pro.sky.telegrambot.model.User();
    private final Long testUser1Id = 1L;
    private final Long testChat1Id = 1L;
    private final Long testUser2Id = 2L;
    private final Long testChat2Id = 2L;

    @BeforeEach
    void init() {
        when(callbackQueryMock.message()).thenReturn(messageMock);
        when(callbackQueryMock.from()).thenReturn(userMock);
        when(messageMock.chat()).thenReturn(chatMock);
        when(messageMock.messageId()).thenReturn(1);
        when(messageMock.from()).thenReturn(userMock);
        when(chatMock.id()).thenReturn(testChat1Id);
        when(userMock.id()).thenReturn(testUser1Id);
        when(telegramBotMock.execute(any())).thenReturn(sendResponseMock);
        when(sendResponseMock.message()).thenReturn(messageMock);
    }

    @Test
    public void sendRequestToEnterPhoneNumberTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.PHONE.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("введите номер телефона для связи в формате - 89093568391");
    }

    @Test
    public void sendRequestToEnterEmailTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.EMAIL.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("введите адрес электронной почты для связи");
    }

    @Test
    public void sendCommunicationOptionMenuTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.COMMUNICATION_REQUEST.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("выберите способ для связи:");
    }

    @Test
    public void sendMenuPreparingForAdoptionCatTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ADOPT_CAT.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи");
    }

    @Test
    public void sendMenuPreparingForAdoptionDogTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ADOPT_DOG.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual1 = argumentCaptor.getValue();
        assertThat(actual1.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual1.getParameters().get("text")).isEqualTo("ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи");

        when(callbackQueryMock.data()).thenReturn(Commands.BACK_START_MENU.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        SendMessage actual2 = argumentCaptor.getValue();
        assertThat(actual2.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual2.getParameters().get("text")).isEqualTo("ознакомьтесь пожалуйста с информацией, которая поможет вам подготовиться ко встрече с новым членом семьи");
    }

    @Test
    public void sendShelterSelectionMenuTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.START.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("выберите какой приют вас интересует");
    }

    public static Stream<Arguments> provideParamsForValidatePhoneNumberTest() {
        String positiveResult = "Запрос на обратную связь успешно отправлен. Наши волонтеры свяжется с вами в ближайшее время.";
        String negativeResult = "Телефонный номер введен не корректно! Повторите пожалуйста ввод.";
        return Stream.of(
                Arguments.of("89035502033", positiveResult),
                Arguments.of("89035502033  ", positiveResult),
                Arguments.of("  89035502033  ", positiveResult),
                Arguments.of("+79026460129", negativeResult),
                Arguments.of("8905-12-834-92", negativeResult),
                Arguments.of("8 905 12 834 92", negativeResult));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForValidatePhoneNumberTest")
    public void validatePhoneNumberTest(String phoneNumberStr, String textMassage) {
        when(callbackQueryMock.data()).thenReturn(Commands.PHONE.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        when(messageMock.text()).thenReturn((phoneNumberStr));
        when(communicationRequestServiceMock.addRequestToDatabase(anyLong(), anyString())).thenReturn(communicationRequestMock);
        out.processMessage(messageMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo(textMassage);
    }

    public static Stream<Arguments> provideParamsForValidateEmailTest() {
        String positiveResult = "Запрос на обратную связь успешно отправлен. Наши волонтеры свяжется с вами в ближайшее время.";
        String negativeResult = "Адрес электронной почты введен не корректно! Повторите пожалуйста ввод.";
        return Stream.of(
                Arguments.of("test@gmail.com", positiveResult),
                Arguments.of("  test_1@mail.ru", positiveResult),
                Arguments.of("test @bk.ru  ", negativeResult),
                Arguments.of("test@mail", negativeResult),
                Arguments.of("@gmail.com", negativeResult),
                Arguments.of("test@mailru", negativeResult));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForValidateEmailTest")
    public void validateEmailTest(String emailStr, String textMassage) {
        when(callbackQueryMock.data()).thenReturn(Commands.EMAIL.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        when(messageMock.text()).thenReturn((emailStr));
        when(communicationRequestServiceMock.addRequestToDatabase(anyLong(), anyString())).thenReturn(communicationRequestMock);
        out.processMessage(messageMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo(textMassage);
    }

    @Test
    public void createCommunicationRequestNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.EMAIL.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        when(messageMock.text()).thenReturn("test@gmail.com");
        when(communicationRequestServiceMock.addRequestToDatabase(anyLong(), anyString())).thenReturn(null);
        out.processMessage(messageMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("Запрос на обратную связь не создан!");
    }

    @Test
    public void sendRequestToEnterDailyReportNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.REPORT_ABOUT_PET.getCommand());
        out.processCallBackQuery(callbackQueryMock);

        when(petOwnerServiceMock.findPetOwnerWithProbationaryPeriod(anyLong())).thenReturn(null);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("вам не нужно отправлять отчет, т.к. вы не являетесь владельцем питомца на испытательном сроке.");
    }

    @Test
    public void sendRequestToEnterDailyReportPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.REPORT_ABOUT_PET.getCommand());
        when(petOwnerServiceMock.findPetOwnerWithProbationaryPeriod(anyLong())).thenReturn(petOwnerMock);
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("пришлите пожалуйста отчет содержащий следующую информацию:\n" +
                "- фото животного\n" +
                "- рацион животного\n" +
                "- общее самочувствие и привыкание к новому месту\n" +
                "- изменение в поведении: отказ от старых привычек, приобретение новых\n" +
                "* Отправьте отчет одним сообщением! Текст отчета должен быть размещен в описании под фото!");
    }

    @Test
    public void sendInfoAboutDogShelterNegativeNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_DOG_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterNegativeNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_DOG_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_DOG_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testInfoText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testInfoText");
    }

    @Test
    public void sendInfoAboutDogShelterDocumentAdoptionNegativeNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterDocumentAdoptionNegativeNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterDocumentAdoptionPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testDocumentText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testDocumentText");
    }

    @Test
    public void sendInfoAboutDogShelterGettingRulesNegativeNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterGettingRulesNegativeNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterGettingRulesPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testRulesText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testRulesText");
    }

    @Test
    public void sendInfoAboutDogShelterTransportationAnimalNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTransportationAnimalNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTransportationAnimalPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testTransportationText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testTransportationText");
    }

    @Test
    public void sendInfoAboutDogShelterHouseCubNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseCubNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseCubPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseCubText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseCubText");
    }

    @Test
    public void sendInfoAboutDogShelterHouseAdultPetNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseAdultPetNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseAdultPetPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DOG_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseAdultPetText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseAdultPetText");
    }

    @Test
    public void sendInfoAboutDogShelterHouseDisablePetNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_DOG_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseDisablePetNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_DOG_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterHouseDisablePetPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_DOG_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseDisablePetText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseDisablePetText");
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TIPS_FROM_DOG_HANDLER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TIPS_FROM_DOG_HANDLER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.TIPS_FROM_DOG_HANDLER, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TIPS_FROM_DOG_HANDLER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.TIPS_FROM_DOG_HANDLER, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testTipsDogHandlerText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testTipsDogHandlerText");
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerListNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDED_DOG_HANDLERS_LIST.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerListNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDED_DOG_HANDLERS_LIST.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RECOMMENDED_DOG_HANDLERS, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterTipsDogHandlerListPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDED_DOG_HANDLERS_LIST.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RECOMMENDED_DOG_HANDLERS, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testTipsDogHandlerListText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testTipsDogHandlerListText");
    }

    @Test
    public void sendInfoAboutDogShelterPossibleReasonForRefusalAdoptionNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterPossibleReasonForRefusalAdoptionNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterPossibleReasonForRefusalAdoptionPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_DOG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testPossibleReasonForRefusalAdoptionText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testPossibleReasonForRefusalAdoptionText");
    }

    @Test
    public void sendInfoAboutDogShelterContactNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterContactNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterContactPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testContactText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testContactText");
    }

    @Test
    public void sendInfoAboutDogShelterSafetyRecommendationNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterSafetyRecommendationNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterSafetyRecommendationPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testSafetyRecommendationText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testSafetyRecommendationText");
    }

    @Test
    public void sendInfoAboutDogShelterPassRegInfoNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterPassRegInfoNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutDogShelterPassRegInfoPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testPassRegText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testPassRegText");
    }

    @Test
    public void sendInfoAboutCatShelterPassRegInfoNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterPassRegInfoNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterPassRegInfoPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_PASS_REG.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_PASS_REG_INFO, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testPassRegText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testPassRegText");
    }

    @Test
    public void sendInfoAboutCatShelterContactNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterContactNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterContactPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_CONTACT_INFO.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SHELTER_CONTACT_INFO, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testContactText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testContactText");
    }

    @Test
    public void sendInfoAboutCatShelterSafetyRecommendationNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterSafetyRecommendationNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterSafetyRecommendationPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.SAFETY_RECOMMENDATIONS, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testSafetyRecommendationText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testSafetyRecommendationText");
    }

    @Test
    public void sendInfoAboutCatShelterGettingRulesNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterGettingRulesNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterGettingRulesPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RULES_FOR_GETTING_TO_KNOW_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.RULES_FOR_GETTING_TO_KNOW_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testGettingRulesText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testGettingRulesText");
    }

    @Test
    public void sendInfoAboutCatShelterDocumentAdoptionNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterDocumentAdoptionNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterDocumentAdoptionPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOCUMENTS_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.DOCUMENTS_FOR_ADOPTION_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testDocumentAdoptionText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testDocumentAdoptionText");
    }

    @Test
    public void sendInfoAboutCatShelterTransportationAnimalNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterTransportationAnimalNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterTransportationAnimalPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.TRANSPORTATION_RECOMMENDATION_FOR_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.ANIMAL_TRANSPORTATION, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testTransportationText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testTransportationText");
    }

    @Test
    public void sendInfoAboutCatShelterHouseCubNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseCubNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseCubPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_CUB_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_CUB, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseCubText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseCubText");
    }

    @Test
    public void sendInfoAboutCatShelterHouseAdultPetNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseAdultPetNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseAdultPetPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_CAT_ADULT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_ADULT_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseAdultPetText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseAdultPetText");
    }

    @Test
    public void sendInfoAboutCatShelterHouseDisablePetNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_CAT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseDisablePetNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_CAT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterHouseDisablePetPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.RECOMMENDATION_FOR_DISABLED_CAT_HOUSE.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.HOUSE_FOR_DISABLED_PET, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseDisablePetText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseDisablePetText");
    }

    @Test
    public void sendInfoAboutCatShelterPossibleReasonForRefusalAdoptionNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterPossibleReasonForRefusalAdoptionNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterPossibleReasonForRefusalAdoptionPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_CAT.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.REASONS_FOR_REFUSAL_OF_ADOPTION, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testHouseDisablePetText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testHouseDisablePetText");
    }

    @Test
    public void sendInfoAboutCatShelterNoShelterTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_CAT_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterNoInfoTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_CAT_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelterMock)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendInfoAboutCatShelterPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.ABOUT_CAT_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        when(infoServiceMock.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelterMock)).thenReturn(infoMock);
        when(infoMock.getText()).thenReturn("testInfoText");
        when(keyboardServiceMock.backDogInfoMenu()).thenReturn(inlineKeyboardMarkupMock);
        out.processCallBackQuery(callbackQueryMock);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text")).isEqualTo("testInfoText");
    }

    @Test
    public void sendDogShelterMenuNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendDogShelterMenuPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text").toString()).startsWith("приют для собак");
    }

    @Test
    public void sendCatShelterMenuNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendCatShelterMenuPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text").toString()).startsWith("приют для кошек");
    }

    @Test
    public void sendCatInfoMenuNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_INFO_MENU.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendCatInfoMenuPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.CAT_SHELTER_INFO_MENU.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.CAT)).thenReturn(shelterMock);
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text").toString()).startsWith("приют для кошек");
    }

    @Test
    public void sendDogInfoMenuNegativeTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_INFO_MENU.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(null);
        assertThat(out.processCallBackQuery(callbackQueryMock)).isNull();
    }

    @Test
    public void sendDogInfoMenuPositiveTest() {
        when(callbackQueryMock.data()).thenReturn(Commands.DOG_SHELTER_INFO_MENU.getCommand());
        when(shelterServiceMock.findByType(TypeOfPet.DOG)).thenReturn(shelterMock);
        out.processCallBackQuery(callbackQueryMock);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBotMock).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(testChat1Id);
        assertThat(actual.getParameters().get("text").toString()).startsWith("приют для собак");
    }
}