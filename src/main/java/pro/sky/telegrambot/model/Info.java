package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String name;

    private String text;

    @OneToMany(mappedBy = "info")
    private List<PictureInfo> pictureInfoList;

    public Info() {
    }

    public Info(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return Objects.equals(name, info.name) && Objects.equals(text, info.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text);
    }
}

