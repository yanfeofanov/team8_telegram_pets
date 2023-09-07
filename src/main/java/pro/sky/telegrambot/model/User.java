package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bot_user")
public class User {
    @Id
    private Long id; //we use id from "telegram" as the primary key for the table
    private Long chatId; //chatId from "telegram"
    private LocalDateTime dateAdded;

    public User() {
    }

    public User(Long id, Long chatId, LocalDateTime dateAdded) {
        this.id = id;
        this.chatId = chatId;
        this.dateAdded = dateAdded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getChatId(), user.getChatId()) && Objects.equals(getDateAdded(), user.getDateAdded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getDateAdded());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", dateAdded=" + dateAdded +
                '}';
    }
}

