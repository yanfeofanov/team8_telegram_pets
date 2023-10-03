package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String name;
    private byte age;
    private String breed;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToOne
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;
    private Boolean leave;

    public Pet() {
    }

    public Pet(String type, String name, byte age, String breed, Shelter shelter, Boolean leave) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.shelter = shelter;
        this.leave = leave;
    }

    public Pet(String type, String name, byte age, String breed, Shelter shelter, PetOwner petOwner, Boolean leave) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.shelter = shelter;
        this.petOwner = petOwner;
        this.leave = leave;
    }

    public Pet(int id, String type, String name, byte age, String breed, Shelter shelter, PetOwner petOwner, Boolean leave) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.shelter = shelter;
        this.petOwner = petOwner;
        this.leave = leave;
    }

    public Boolean getLeave() {
        return leave;
    }

    public void setLeave(Boolean leave) {
        this.leave = leave;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return getId() == pet.getId() && getAge() == pet.getAge() && Objects.equals(getType(), pet.getType()) && Objects.equals(getName(), pet.getName()) && Objects.equals(getBreed(), pet.getBreed()) && Objects.equals(getShelter(), pet.getShelter()) && Objects.equals(getPetOwner(), pet.getPetOwner()) && Objects.equals(getLeave(), pet.getLeave());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getName(), getAge(), getBreed(), getShelter(), getPetOwner(), getLeave());
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                ", shelter=" + shelter +
                ", petOwner=" + petOwner +
                ", leave=" + leave +
                '}';
    }
}

