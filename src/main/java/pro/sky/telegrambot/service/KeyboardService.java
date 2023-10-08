package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
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
    private InlineKeyboardMarkup prepareMultilineKeyboard(List<Commands> commandList) {
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
    private InlineKeyboardMarkup prepareSinglelineKeyboard(List<Commands> commandList) {
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

    public InlineKeyboardMarkup generateShelterSelectionMenu() {
        List<Commands> commandList = List.of(Commands.CAT_SHELTER, Commands.DOG_SHELTER);
        return prepareSinglelineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateMenuPreparingForAdoption(TypeOfPet typeOfPet) {
        List<Commands> commandList = new ArrayList<>();
        if (TypeOfPet.DOG.equals(typeOfPet)) {
            commandList.add(Commands.RULES_FOR_GETTING_TO_KNOW_DOG);
            commandList.add(Commands.DOCUMENTS_FOR_ADOPTION_DOG);
            commandList.add(Commands.TRANSPORTATION_RECOMMENDATION_FOR_DOG);
            commandList.add(Commands.RECOMMENDATION_FOR_DOG_CUB_HOUSE);
            commandList.add(Commands.RECOMMENDATION_FOR_DOG_ADULT_HOUSE);
            commandList.add(Commands.RECOMMENDATION_FOR_DISABLED_DOG_HOUSE);
            commandList.add(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_DOG);
            commandList.add(Commands.TIPS_FROM_DOG_HANDLER);
            commandList.add(Commands.RECOMMENDED_DOG_HANDLERS_LIST);
            commandList.add(Commands.COMMUNICATION_REQUEST);
            commandList.add(Commands.CALL_VOLUNTEER);
            commandList.add(Commands.BACK_DOG_SHELTER);
        } else {
            commandList.add(Commands.RULES_FOR_GETTING_TO_KNOW_CAT);
            commandList.add(Commands.DOCUMENTS_FOR_ADOPTION_CAT);
            commandList.add(Commands.TRANSPORTATION_RECOMMENDATION_FOR_CAT);
            commandList.add(Commands.RECOMMENDATION_FOR_CAT_CUB_HOUSE);
            commandList.add(Commands.RECOMMENDATION_FOR_CAT_ADULT_HOUSE);
            commandList.add(Commands.RECOMMENDATION_FOR_DISABLED_CAT_HOUSE);
            commandList.add(Commands.POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION_CAT);
            commandList.add(Commands.COMMUNICATION_REQUEST);
            commandList.add(Commands.CALL_VOLUNTEER);
            commandList.add(Commands.BACK_CAT_SHELTER);
        }
        return prepareMultilineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateMainDogShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.DOG_SHELTER_INFO_MENU,
                Commands.ADOPT_DOG,
                Commands.REPORT_ABOUT_PET,
                Commands.CALL_VOLUNTEER,
                Commands.BACK_START_MENU);
        return prepareMultilineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateInfoDogShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_DOG_SHELTER,
                Commands.DOG_SHELTER_CONTACT_INFO,
                Commands.DOG_SHELTER_PASS_REG,
                Commands.DOG_SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER,
                Commands.BACK_DOG_SHELTER);
        return prepareMultilineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateMainCatShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.CAT_SHELTER_INFO_MENU,
                Commands.ADOPT_CAT,
                Commands.REPORT_ABOUT_PET,
                Commands.CALL_VOLUNTEER,
                Commands.BACK_START_MENU);
        return prepareMultilineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateInfoCatShelterMenu() {
        List<Commands> commandList = List.of(
                Commands.ABOUT_CAT_SHELTER,
                Commands.CAT_SHELTER_CONTACT_INFO,
                Commands.CAT_SHELTER_PASS_REG,
                Commands.CAT_SHELTER_SAFETY_RECOMMENDATIONS,
                Commands.COMMUNICATION_REQUEST,
                Commands.CALL_VOLUNTEER,
                Commands.BACK_CAT_SHELTER);
        return prepareMultilineKeyboard(commandList);
    }

    public InlineKeyboardMarkup generateCommunicationOptionMenu() {
        List<Commands> commandList = List.of(Commands.PHONE, Commands.EMAIL);
        return prepareSinglelineKeyboard(commandList);
    }

    public InlineKeyboardMarkup backButtonMenuDog() {
        List<Commands> commandsList = List.of(Commands.BACK_DOG_SHELTER);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup backButtonMenuCat() {
        List<Commands> commandsList = List.of(Commands.BACK_CAT_SHELTER);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup backCatInfoMenu() {
        List<Commands> commandsList = List.of(Commands.BACK_CAT_INFO_MENU);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup backDogInfoMenu() {
        List<Commands> commandsList = List.of(Commands.BACK_DOG_INFO_MENU);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup backDogAdoptionMenu() {
        List<Commands> commandsList = List.of(Commands.BACK_DOG_ADOPTION_MENU);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup backCatAdoptionMenu() {
        List<Commands> commandsList = List.of(Commands.BACK_CAT_ADOPTION_MENU);
        return prepareMultilineKeyboard(commandsList);
    }

    public InlineKeyboardMarkup linkToBotUserButton(Long userId) {
        String button_url = "tg://user?id=" + userId;
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineButton = new InlineKeyboardButton("пользователь");
        inlineButton.url(button_url);
        keyboard.addRow(inlineButton);
        return keyboard;
    }
}
