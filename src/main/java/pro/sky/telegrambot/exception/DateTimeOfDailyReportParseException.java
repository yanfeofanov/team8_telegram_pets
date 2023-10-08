package pro.sky.telegrambot.exception;

public class DateTimeOfDailyReportParseException extends BadParamException {
    public DateTimeOfDailyReportParseException() {
    }

    @Override
    public String getMessage() {
        return "Дата указана неверно";
    }
}
