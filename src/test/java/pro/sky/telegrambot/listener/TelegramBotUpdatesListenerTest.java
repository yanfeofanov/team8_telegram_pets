package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import pro.sky.telegrambot.service.TelegramBotService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    Logger loggerMock = mock(Logger.class);
    Update updateMock = mock(Update.class);
    Message messageMock = mock(Message.class);
    CallbackQuery callbackQueryMock = mock(CallbackQuery.class);
    Chat chatMock = mock(Chat.class);
    User userMock = mock(User.class);
    TelegramBot telegramBotMock = mock(TelegramBot.class);
    TelegramBotService telegramBotServiceMock = mock(TelegramBotService.class);
    @InjectMocks
    TelegramBotUpdatesListener out = new TelegramBotUpdatesListener(telegramBotMock, telegramBotServiceMock);

    @Test
    void processTest() {
        List<Update> updates = List.of(updateMock, updateMock, updateMock, updateMock, updateMock);
        when(messageMock.chat()).thenReturn(chatMock);
        when(messageMock.from()).thenReturn(userMock);
        when(callbackQueryMock.from()).thenReturn(userMock);
        when(chatMock.id()).thenReturn(1L);
        when(userMock.isBot()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true);
        when(updateMock.message()).thenReturn(messageMock).thenReturn(null).thenReturn(messageMock).thenReturn(null).thenReturn(messageMock);
        when(updateMock.callbackQuery()).thenReturn(null).thenReturn(callbackQueryMock).thenReturn(callbackQueryMock);
        when(telegramBotServiceMock.processMessage(any())).thenReturn(0).thenReturn(1);
        ReflectionTestUtils.setField(out, "logger", loggerMock);
        out.process(updates);
        verify(telegramBotServiceMock, new Times(2)).processMessage(any());
        verify(telegramBotServiceMock, new Times(1)).processCallBackQuery(any());
        verify(loggerMock, new Times(1)).error(any());
    }
}