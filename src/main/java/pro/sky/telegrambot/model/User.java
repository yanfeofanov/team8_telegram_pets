package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bot_user")
public class User {
    @Id
    private Long id; //we use id from "telegram" as the primary key for the table
    @JoinColumn(name = "user_name")
    private String userName;
    @JoinColumn(name = "chat_id")
    private Long chatId; //chatId from "telegram"
    @JoinColumn(name = "date_added")
    private LocalDateTime dateAdded;

    public User() {
    }

    public User(Long id, String userName, Long chatId, LocalDateTime date_Added) {
        this.id = id;
        this.userName = userName;
        this.chatId = chatId;
        this.dateAdded = date_Added;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDate_Added() {
        return dateAdded;
    }

    public void setDate_Added(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getUserName(), user.getUserName())
                && Objects.equals(getChatId(), user.getChatId()) && Objects.equals(dateAdded, user.dateAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getChatId(), dateAdded);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName='" + userName + '\'' +
                ", chatId=" + chatId +
                ", dateAdded=" + dateAdded +
                '}';
    }
}

