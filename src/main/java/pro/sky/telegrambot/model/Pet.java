package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    private String type;
    private String breed;

    @OneToMany(mappedBy = "pet")
    private List<PhotoPet> photoPets;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Pet() {
    }

    public Pet(Long id, String name, int age, String type, String breed) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return age == pet.age && Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && Objects.equals(type, pet.type) && Objects.equals(breed, pet.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, type, breed);
    }

    @Override
    public String toString() {
        return id + name + age + type + breed;
    }
}

