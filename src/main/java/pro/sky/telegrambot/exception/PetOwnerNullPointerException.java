package pro.sky.telegrambot.exception;

public class PetOwnerNullPointerException extends RuntimeException{
    private final long id;

    public PetOwnerNullPointerException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Владелец с id: " + id + " не найден";
    }
}
