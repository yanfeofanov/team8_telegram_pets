package pro.sky.telegrambot.model;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String name;
    private String phone;
    private String mail;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public User() {
    }

    public User(Long chatId, String name, String phone, String mail) {
        this.chatId = chatId;
        this.name = StringUtils.capitalize(name.toUpperCase());
        this.phone = phone;
        this.mail = mail;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.capitalize(name.toUpperCase());
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : 0;
    }
}

