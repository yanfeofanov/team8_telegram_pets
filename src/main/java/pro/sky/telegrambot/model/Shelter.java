package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table (name = "shelters")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "shelter")
    private List <Pet> pets;

    @OneToMany(mappedBy = "shelter")
    private List<User> users;

    public Shelter() {
    }

    public Shelter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Shelter(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id == shelter.id && Objects.equals(name, shelter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "name='" + name + '\'' +
                '}';
    }
}

