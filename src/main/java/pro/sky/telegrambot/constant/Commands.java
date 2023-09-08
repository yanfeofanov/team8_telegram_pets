package pro.sky.telegrambot.constant;

public enum Commands {

    START("старт бота", "/start"),

    CAT_SHELTER("приют для кошек", "/cat_shelter"),

    DOG_SHELTER("приют для собак", "/dog_shelter");
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
