package pro.sky.telegrambot.model;

import pro.sky.telegrambot.constant.TypesOfInformation;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private TypesOfInformation type;
    private String text;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Info() {
    }

    public Info(TypesOfInformation type, String text, Shelter shelter) {
        this.type = type;
        this.text = text;
        this.shelter = shelter;
    }

    public int getId() {
        return id;
    }

    public TypesOfInformation getType() {
        return type;
    }

    public void setType(TypesOfInformation type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info)) return false;
        Info info = (Info) o;
        return getId() == info.getId() && Objects.equals(getType(), info.getType()) && Objects.equals(getText(), info.getText()) && Objects.equals(getShelter(), info.getShelter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getText(), getShelter());
    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", shelter=" + shelter +
                '}';
    }
}


