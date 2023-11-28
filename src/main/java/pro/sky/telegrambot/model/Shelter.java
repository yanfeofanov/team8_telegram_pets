package pro.sky.telegrambot.model;

import pro.sky.telegrambot.constant.TypeOfPet;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shelter")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypeOfPet type;

    public Shelter() {
    }

    public Shelter(String name, TypeOfPet type) {
        this.name = name;
        this.type = type;
    }

    public Shelter(int id, String name, TypeOfPet type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfPet getType() {
        return type;
    }

    public void setType(TypeOfPet type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelter)) return false;
        Shelter shelter = (Shelter) o;
        return getId() == shelter.getId() && Objects.equals(getName(), shelter.getName()) && getType() == shelter.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType());
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}

