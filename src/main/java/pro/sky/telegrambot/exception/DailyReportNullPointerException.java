package pro.sky.telegrambot.exception;

public class DailyReportNullPointerException extends RuntimeException{

    private final long id;

    public DailyReportNullPointerException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Отчет с id: " + id + " не найден";
    }
}
