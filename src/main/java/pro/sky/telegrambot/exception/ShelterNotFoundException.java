package pro.sky.telegrambot.exception;

public class ShelterNotFoundException extends RuntimeException {
    public ShelterNotFoundException() {
    }

    public ShelterNotFoundException(String message) {
        super(message);
    }
}
