package pro.sky.telegrambot.constant;

public enum ProbationStatus {
    SUCCESS("Испытательный срок пройден"),
    SHORT_STATUS("Продлить испытательный срок на 14 дней"),
    LONG_STATUS("Продлить испытательный срок на 30 дней");

    private final String text;

    ProbationStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
