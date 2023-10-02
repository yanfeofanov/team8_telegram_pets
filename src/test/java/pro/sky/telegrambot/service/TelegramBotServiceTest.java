package pro.sky.telegrambot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
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
    private final BaseResponse baseResponseMock = mock(BaseResponse.class);
    private final SendResponse sendResponseMock = mock(SendResponse.class);
    static final pro.sky.telegrambot.model.User botUser = new pro.sky.telegrambot.model.User();
    private final Long testUser1Id = 0L;
    private final Long testUser2Id = 1L;
    private final Long testChat1Id = 0L;
    private final Long testChat2Id = 1L;

    @BeforeEach
    void init() {
        when(telegramBotMock.execute(any(SendMessage.class))).thenReturn(sendResponseMock);
    }
    @Test
    void processMessageTest() {
        when(messageMock.from()).thenReturn(userMock);
        when(userMock.id()).thenReturn(testUser1Id);
        when(messageMock.chat()).thenReturn(chatMock);
        when(chatMock.id()).thenReturn(testChat1Id);
        when(messageMock.text()).thenReturn("/start");
        when(userServiceMock.isTheUserNew(1L)).thenReturn(false);

        //when(userServiceMock.addNewUser(anyLong(), anyString(), anyString(), anyLong())).thenReturn(new pro.sky.telegrambot.model.User());

        //        when(out.sendReply(any(), any())).thenReturn(baseResponseMock);
//        when(sendResponseMock.errorCode()).thenReturn(errorCode);
//        assertEquals(out.processMessage(any(Message.class)), errorCode);
//        verify(userServiceMock, new Times(numberOfCallsAddNewUser)).addNewUser(userId, any(), any(), chatId);
//        verify(out, new Times(numberOfCallsAddNewUser)).pro(userId, any(), any(), chatId);
    }

    @Test
    void sendReplyTest() {

    }
}