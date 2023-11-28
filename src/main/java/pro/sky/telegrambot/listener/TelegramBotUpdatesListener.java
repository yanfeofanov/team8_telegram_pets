package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TelegramBotService telegramBotService) {
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            CallbackQuery callbackQuery = update.callbackQuery();
            BaseResponse response;
            if (message != null && !message.from().isBot()) {
                response = telegramBotService.processMessage(message);
                if (response == null || !response.isOk()) {
                    logger.error("the message id: " + message.messageId()
                            + " from user #" + message.chat().id()
                            + " was not processed correctly");
                }
            } else if (callbackQuery != null && !callbackQuery.from().isBot()) {
                response = telegramBotService.processCallBackQuery(callbackQuery);
                if (response == null || !response.isOk()) {
                    logger.error("the callbackQuery id: " + callbackQuery.id()
                            + " from user #" + callbackQuery.from().id()
                            + " was not processed correctly");
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
