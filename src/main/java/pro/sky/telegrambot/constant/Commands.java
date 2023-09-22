package pro.sky.telegrambot.constant;

public enum Commands {

    START("запуск бота", "/start"),
    HELP("описание команд бота", "/help"),
    CAT_SHELTER("приют для кошек", "/cat_shelter"),
    DOG_SHELTER("приют для собак", "/dog_shelter"),
    CAT_SHELTER_MENU("меню информации о приюте кошек", "/cat_shelter_info_menu"),
    DOG_SHELTER_MENU("меню информации о приюте собак", "/dog_shelter_info_menu"),
    ADOPT_CAT("информация для усыновителя кошки", "/preparing_for_adoption_dog"),
    ADOPT_DOG("информация для усыновителя собаки", "/preparing_for_adoption_cat"),
    ABOUT_CAT_SHELTER("информация о приюте для кошек", "about_cat_shelter"),
    ABOUT_DOG_SHELTER("информация о приюте для собак", "about_dog_shelter"),
    CAT_SHELTER_CONTACT_INFO("расписание, адрес и схема проезда", "cat_shelter_contact_info"),
    DOG_SHELTER_CONTACT_INFO("расписание, адрес и схема проезда", "dog_shelter_contact_info"),
    CAT_SHELTER_PASS_REG("контакты для оформления пропуска","cat_shelter_pass_reg"),
    DOG_SHELTER_PASS_REG("контакты для оформления пропуска","dog_shelter_pass_reg"),
    DOG_SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности в приюте", "shelter_safety_recommendations"),
    CAT_SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности в приюте", "cat_shelter_safety_recommendations"),
    RULES_FOR_GETTING_TO_KNOW_PET("правила знакомства с питомцем", "rules_for_getting_to_know_pet"),
    DOCUMENTS_FOR_ADOPTION("документы для усыновления питомца", "documents_for_adoption"),
    TRANSPORTATION_RECOMMENDATION("транспортировка животного", "transportation_recommendations"),
    RECOMMENDATION_FOR_CUB_HOUSE("домик щенка/котенка", "recommendations_for_cub_house"),
    RECOMMENDATION_FOR_ADULT_PET_HOUSE("домик взрослой собаки/кошки", "recommendations_for_adult_pet_house"),
    RECOMMENDATION_FOR_DISABLED_PET_HOUSE("домик питомца с инвалидностью", "recommendations_for_disabled_pet_house"),
    TIPS_FROM_DOG_HANDLER("советы кинолога", "tips_from_dog_handler"),
    RECOMMENDED_DOG_HANDLERS_LIST("рекомендуемые кинологи", "recommended_dog_handler_list"),
    POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION("причины отказа в усыновлении", "possible_reasons_for_refusal_of_adoption"),
    REPORT_ABOUT_PET("отправить отчет о питомце", "/report_about_pet"),
    COMMUNICATION_REQUEST("запрос на обратную связь", "/communication_request"),
    CALL_VOLUNTEER("позвать волонтера", "/call_volunteer"),
    PHONE("телефон", "phone"),
    EMAIL("электронная почта", "email"),
    BACK_DOG_SHELTER("вернуться в приют для собак", "back_dog_shelter"),
    BACK_CAT_SHELTER("вернуться в приют для кошек", "back_cat_shelter");




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
