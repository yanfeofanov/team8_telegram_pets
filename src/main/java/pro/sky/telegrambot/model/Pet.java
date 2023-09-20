package pro.sky.telegrambot.model;

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
    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public Pet() {
    }

    public Pet(String type, String name, byte age, String breed, Shelter shelter, PetOwner petOwner, Photo photo) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.shelter = shelter;
        this.petOwner = petOwner;
        this.photo = photo;
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return getAge() == pet.getAge() && Objects.equals(getId(), pet.getId()) && Objects.equals(getType(), pet.getType()) && Objects.equals(getName(), pet.getName()) && Objects.equals(getBreed(), pet.getBreed()) && Objects.equals(getShelter(), pet.getShelter()) && Objects.equals(getPetOwner(), pet.getPetOwner()) && Objects.equals(getPhoto(), pet.getPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getName(), getAge(), getBreed(), getShelter(), getPetOwner(), getPhoto());
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
                ", photo=" + photo +
                '}';
    }
}

