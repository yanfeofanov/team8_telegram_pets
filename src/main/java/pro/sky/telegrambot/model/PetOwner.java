package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "pet_owner")
public class PetOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;

    @OneToOne
    @JoinColumn(name = "bot_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
    private boolean probation;
    @Column(name = "end_probation")
    private LocalDateTime endProbation;

    public PetOwner() {
    }

    public PetOwner(String name, String surname, String phoneNumber, String email, User user, Volunteer volunteer, boolean probation, LocalDateTime endProbation) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.user = user;
        this.volunteer = volunteer;
        this.probation = probation;
        this.endProbation = endProbation;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public boolean isProbation() {
        return probation;
    }

    public void setProbation(boolean probation) {
        this.probation = probation;
    }

    public LocalDateTime getEndProbation() {
        return endProbation;
    }

    public void setEndProbation(LocalDateTime endProbation) {
        this.endProbation = endProbation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetOwner)) return false;
        PetOwner petOwner = (PetOwner) o;
        return getId() == petOwner.getId() && isProbation() == petOwner.isProbation() && Objects.equals(getName(),
                petOwner.getName()) && Objects.equals(getSurname(), petOwner.getSurname()) && Objects.equals(getPhoneNumber(),
                petOwner.getPhoneNumber()) && Objects.equals(getEmail(), petOwner.getEmail()) && Objects.equals(getUser(),
                petOwner.getUser()) && Objects.equals(getVolunteer(), petOwner.getVolunteer()) && Objects.equals(getEndProbation(), petOwner.getEndProbation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getPhoneNumber(), getEmail(), getUser(), getVolunteer(), isProbation(), getEndProbation());
    }

    @Override
    public String toString() {
        return "PetOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                ", volunteer=" + volunteer +
                ", probation=" + probation +
                ", endProbation=" + endProbation +
                '}';
    }
}
