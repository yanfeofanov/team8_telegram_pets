package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.sky.telegrambot.constant.Commands;

@Configuration

public class TelegramBotConfiguration {

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
        BotCommand[] botCommands = new BotCommand[Commands.values().length];
        Commands[] commands = Commands.values();
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].getCommand().contains("/")) {
                botCommands[i] = new BotCommand(commands[i].getCommand(), commands[i].getDescription());
            }
        }
        return botCommands;
    }

}
