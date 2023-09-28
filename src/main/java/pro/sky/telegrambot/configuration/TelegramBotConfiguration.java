package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.sky.telegrambot.constant.Commands;

import java.util.ArrayList;
import java.util.List;

@Configuration

public class  TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        bot.execute(new SetMyCommands(generateListOfCommands()));
        return bot;
    }

    private BotCommand[] generateListOfCommands() {
        List<BotCommand> botCommandsList = new ArrayList<>();
        Commands[] commands = Commands.values();
        for (Commands command : commands) {
            if (command.getCommand().contains("/")) {
                botCommandsList.add(new BotCommand(command.getCommand(), command.getDescription()));
            }
        }
        return botCommandsList.toArray(BotCommand[]::new);
    }

}
