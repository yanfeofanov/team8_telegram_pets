package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;

import java.util.List;

@Service
public class KeyboardService {

    public Keyboard prepareInlineKeyboard(List<Commands> commandList) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        for (Commands command : commandList) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(command.getDescription());
            inlineButton.callbackData(command.getCommand());
            keyboard.addRow(inlineButton);
        }
        return keyboard;
    }
}
