package pro.sky.telegrambot.constant;

public enum Commands {

    START("запуск бота", "/start"),
    HELP("описание команд бота", "/help"),
    CAT_SHELTER("выбор приюта для кошек", "/cat_shelter"),
    DOG_SHELTER("выбор приюта для собак", "/dog_shelter"),
    ABOUT_CAT_SHELTER("информация о приюте для кошек", "/about_cat_shelter"),
    ABOUT_DOG_SHELTER("информация о приюте для собак", "/about_dog_shelter"),
    CAT_SHELTER_CONTACT_INFO("расписание работы приюта для кошек, адрес и схема проезда", "/cat_shelter_contact_info"),
    DOG_SHELTER_CONTACT_INFO("расписание работы приюта для собак, адрес и схема проезда", "/dog_shelter_contact_info"),
    CAT_SHELTER_PASS_REG("контакты охраны приюта для кошек, для оформления пропуска на машину","/cat_shelter_pass_reg"),
    DOG_SHELTER_PASS_REG("контанты охраны приюта для собак, для оформления пропуска на машину","/dog_shelter_pass_reg"),
    SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности на территории приюта", "/shelter_safety_recommendations"),
    COMMUNICATION_REQUEST("запрос на обратную связь", "/communication_request"),
    CALL_VOLUNTEER("позвать волонтера", "/call_volunteer");

    private final String description;
    private final String command;

    Commands(String description, String command) {
        this.description = description;
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }
}
