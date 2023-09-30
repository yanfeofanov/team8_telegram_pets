package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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
    private final User userMock = mock(User.class);
    private final Chat chatMock = mock(Chat.class);
    private final SendResponse sendResponseMock = mock(SendResponse.class);
    static final Long user1Id = 0L;
    static final Long user2Id = 1L;
    static final Long chat1Id = 0L;
    static final Long chat2Id = 1L;
    static final pro.sky.telegrambot.model.User botUser = new pro.sky.telegrambot.model.User();

   /* public static Stream<Arguments> provideParamsForTestProcessMessage() {

        return Stream.of(
                Arguments.of("start/", true, 1, 1, 0),
                Arguments.of("start/", false, 0, 1, 0),
                Arguments.of("text", false, , , ),
                Arguments.of(null, , , , , , , ));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTestProcessMessage")
    void TestProcessMessage(String textMessage, boolean newUser,
                            int numberOfCallsAddNewUser, int numberOfCallsProcessCommand,
                            int numberOfCallsProcessInputOfInformation) {
        int errorCode = 0;
        Long userId = 1L
        Long chatId = 1L;
        when(messageMock.from()).thenReturn(userMock);
        when(messageMock.chat()).thenReturn(chatMock);
        when(userMock.id()).thenReturn(userId);
        when(chatMock.id()).thenReturn(chatId);
        when(messageMock.text()).thenReturn(textMessage);
        when(userServiceMock.isTheUserNew(1L)).thenReturn(newUser);
        when(out.sendReply(any(), any())).thenReturn(sendResponseMock);
        when(sendResponseMock.errorCode()).thenReturn(errorCode);
        assertEquals(out.processMessage(any(Message.class)), errorCode);
        verify(userServiceMock, new Times(numberOfCallsAddNewUser)).addNewUser(userId, any(), any(), chatId);
        verify(out, new Times(numberOfCallsAddNewUser)).pro(userId, any(), any(), chatId);
    }*/
}