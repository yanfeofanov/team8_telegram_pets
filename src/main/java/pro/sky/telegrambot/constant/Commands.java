package pro.sky.telegrambot.constant;

public enum Commands {

    START("запуск бота", "/start"),
    HELP("описание команд бота", "/help"),
    CAT_SHELTER("приют для кошек", "/cat_shelter"),
    DOG_SHELTER("приют для собак", "/dog_shelter"),
    ABOUT_CAT_SHELTER("информация о приюте для кошек", "/about_cat_shelter"),
    ABOUT_DOG_SHELTER("информация о приюте для собак", "/about_dog_shelter"),
    CAT_SHELTER_CONTACT_INFO("расписание работы приюта для кошек, адрес и схема проезда", "/cat_shelter_contact_info"),
    DOG_SHELTER_CONTACT_INFO("расписание работы приюта для собак, адрес и схема проезда", "/dog_shelter_contact_info"),
    CAT_SHELTER_PASS_REG("контакты для оформления пропуска на машину в приют кошек","/cat_shelter_pass_reg"),
    DOG_SHELTER_PASS_REG("контакты для оформления пропуска на машину в приют собак","/dog_shelter_pass_reg"),
    SHELTER_SAFETY_RECOMMENDATIONS("техника безопасности на территории приюта", "/shelter_safety_recommendations"),
    COMMUNICATION_REQUEST("запрос на обратную связь", "/communication_request"),
    CALL_VOLUNTEER("позвать волонтера", "/call_volunteer"),
    ADOPT_CAT("информация для готовящихся взять кошку", "/preparing_for_adoption_dog"),
    ADOPT_DOG("информация для готовящихся взять собаку", "/preparing_for_adoption_cat"),
    RULES_FOR_GETTING_TO_KNOW_PET("правила знакомства с питомцем", "/rules_for_getting_to_know_pet"),
    DOCUMENTS_FOR_ADOPTION("документы необходимые для усыновления питомца", "documents_for_adoption"),
    TRANSPORTATION_RECOMMENDATION("рекомндации по транспортировке животного", "/transportation_recommendations"),
    RECOMMENDATION_FOR_CUB_HOUSE("рекомендации по обустройства домика щенка/котенка", "/recommendations_for_cub_house"),
    RECOMMENDATION_FOR_ADULT_PET_HOUSE("рекомендации по обустройства домика взрослой собаки/кошки", "/recommendations_for_adult_pet_house"),
    RECOMMENDATION_FOR_DISABLED_PET_HOUSE("рекомендации по обустройства домика питомца с ограниченными возможностями", "/recommendations_for_disabled_pet_house"),
    TIPS_FROM_DOG_HANDLER("советы кинолога по первичному общению с собакой", "/tips_from_dog_handler"),
    RECOMMENDED_DOG_HANDLERS_LIST("список рекомендуемых нами кинологов", "/recomended_dog_handler_list"),
    POSSIBLE_REASON_FOR_REFUSAL_FOR_ADOPTION("возможные причины отказа в усыновлении питомца", "/possible_reasons_for_refusal_of_adoption");

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
