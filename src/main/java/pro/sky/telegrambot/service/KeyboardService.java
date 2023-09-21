package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.Commands;
import pro.sky.telegrambot.constant.TypeOfPet;

import java.util.ArrayList;
import java.util.List;

/**
 * класс содержит методы по роботе с меню для данного telegram бота
 */
@Service
public class KeyboardService {

    /**
     * метод формирует многострочное меню для чата телеграмм бота на основе входящего списка команд(пунктов меню)
     *
     * @param commandList список значений перечисления Command на основе которых формируется меню
     * @return объект-меню для telegram бота
     */
    private Keyboard prepareMultilineKeyboard(List<Commands> commandList) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        for (Commands command : commandList) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(command.getDescription());
            inlineButton.callbackData(command.getCommand());
            keyboard.addRow(inlineButton);
        }
        return keyboard;
    }

    /**
     * метод формирует однострочное меню для чата телеграмм бота на основе входящего списка команд(пунктов меню)
     *
     * @param commandList список значений перечисления Command на основе которых формируется меню
     * @return объект-меню для telegram бота
     */
    private Keyboard prepareSinglelineKeyboard(List<Commands> commandList) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[commandList.size()];
        for (int i = 0; i < commandList.size(); i++) {

            InlineKeyboardButton inlineButton = new InlineKeyboardButton(commandList.get(i).getDescription());
            inlineButton.callbackData(commandList.get(i).getCommand());
            buttons[i] = inlineButton;
        }
        keyboard.addRow(buttons);
        return keyboard;
    }

    public Keyboard generateShelterSelectionMenu() {
        List<Commands> commandList = List.of(Commands.CAT_SHELTER, Commands.DOG_SHELTER);
        return prepareSinglelineKeyboard(commandList);
    }

    public Keyboard generateMenuPreparingForAdoption(TypeOfPet typeOfPet) {
        List<Commands> commandList = new ArrayList<>();
        commandList.add(Commands.RULES_FOR_GETTING_TO_KNOW_PET);
        commandList.add(Commands.DOCUMENTS_FOR_ADOPTION);
        commandList.add(Commands.TRANSPORTATION_RECOMMENDATION);
        commandList.add(Commands.RECOMMENDATION_FOR_CUB_HOUSE);
        commandList.add(Commands.RECOMMENDATION_FOR_ADULT_PET_HOUSE);
        commandList.add(Commands.RECOMMENDATION_FOR_DISABLED_PET_HOUSE);
        commandList.add(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION);
        if (TypeOfPet.DOG.equals(typeOfPet)) {
            commandList.add(Commands.TIPS_FROM_DOG_HANDLER);
            commandList.add(Commands.RECOMMENDED_DOG_HANDLERS_LIST);
            commandList.add(Commands.COMMUNICATION_REQUEST);
            commandList.add(Commands.CALL_VOLUNTEER);
        }
        return prepareMultilineKeyboard(commandList);
    }

    public Keyboard generateDogShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_DOG_SHELTER,
                Commands.DOG_SHELTER_CONTACT_INFO,
                Commands.DOG_SHELTER_PASS_REG,
                Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.ADOPT_DOG,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER);
        return prepareMultilineKeyboard(commandList);
    }

    public Keyboard generateCatShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_CAT_SHELTER,
                Commands.CAT_SHELTER_CONTACT_INFO,
                Commands.CAT_SHELTER_PASS_REG,
                Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.ADOPT_CAT,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER);
        return prepareMultilineKeyboard(commandList);
    }

    public Keyboard generateCommunicationOptionMenu() {
        List<Commands> commandList = List.of(Commands.PHONE, Commands.EMAIL);
        return prepareSinglelineKeyboard(commandList);
    }

    public Keyboard backButtonMenuDog() {
        List<Commands> commandsList = List.of(Commands.BACK_DOG_SHELTER);
        return prepareMultilineKeyboard(commandsList);
    }

    public Keyboard backButtonMenuCat() {
        List<Commands> commandsList = List.of(Commands.BACK_CAT_SHELTER);
        return prepareMultilineKeyboard(commandsList);
    }

}
