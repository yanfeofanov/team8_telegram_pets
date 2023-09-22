package pro.sky.telegrambot.constant;

public enum Commands {

    START("запуск бота", "/start"),
    HELP("описание команд бота", "/help"),
    CAT_SHELTER("приют для кошек", "/cat_shelter"),
    DOG_SHELTER("приют для собак", "/dog_shelter"),
    CAT_SHELTER_MENU("меню с информацией о приюте", "/cat_shelter_info_menu"),
    DOG_SHELTER_MENU("меню с информацией о приюте", "/dog_shelter_info_menu"),
    ABOUT_CAT_SHELTER("информация о приюте для кошек", "about_cat_shelter"),
    ABOUT_DOG_SHELTER("информация о приюте для собак", "about_dog_shelter"),
    CAT_SHELTER_CONTACT_INFO("расписание, адрес и схема проезда", "cat_shelter_contact_info"),
    DOG_SHELTER_CONTACT_INFO("расписание, адрес и схема проезда", "dog_shelter_contact_info"),
    CAT_SHELTER_PASS_REG("контакты для оформления пропуска","cat_shelter_pass_reg"),
    DOG_SHELTER_PASS_REG("контакты для оформления пропуска","dog_shelter_pass_reg"),
    DOG_SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности на территории приюта", "shelter_safety_recommendations"),
    ADOPT_CAT("информация для готовящихся взять кошку", "/preparing_for_adoption_dog"),
    ADOPT_DOG("информация для готовящихся взять собаку", "/preparing_for_adoption_cat"),
    RULES_FOR_GETTING_TO_KNOW_PET("правила знакомства с питомцем", "rules_for_getting_to_know_pet"),
    REPORT_ABOUT_PET("отправить отчет о питомце", "/report_about_pet"),
    COMMUNICATION_REQUEST("запрос на обратную связь", "/communication_request"),
    CALL_VOLUNTEER("позвать волонтера", "/call_volunteer"),
    DOCUMENTS_FOR_ADOPTION("документы необходимые для усыновления питомца", "documents_for_adoption"),
    TRANSPORTATION_RECOMMENDATION("рекомндации по транспортировке животного", "transportation_recommendations"),
    RECOMMENDATION_FOR_CUB_HOUSE("обустройство домика щенка/котенка", "recommendations_for_cub_house"),
    RECOMMENDATION_FOR_ADULT_PET_HOUSE("обустройство домика взрослой собаки/кошки", "recommendations_for_adult_pet_house"),
    RECOMMENDATION_FOR_DISABLED_PET_HOUSE("обустройство домика питомца с ограниченными возможностями", "recommendations_for_disabled_pet_house"),
    TIPS_FROM_DOG_HANDLER("советы кинолога по первичному общению с собакой", "tips_from_dog_handler"),
    RECOMMENDED_DOG_HANDLERS_LIST("список рекомендуемых нами кинологов", "recommended_dog_handler_list"),
    POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION("возможные причины отказа в усыновлении питомца", "possible_reasons_for_refusal_of_adoption"),
    PHONE("телефон", "phone"),
    EMAIL("электронная почта", "email"),
    BACK_DOG_SHELTER("вернуться в приют для собак", "back_dog_shelter"),
    BACK_CAT_SHELTER("вернуться в приют для кошек", "back_cat_shelter"),
    CAT_SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности на территории приюта", "cat_shelter_safety_recommendations");




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
