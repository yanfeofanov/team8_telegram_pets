package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "communication_request")
public class CommunicationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "bot_user_id")
    private User user;
    @Column(name = "contact_info")
    private String contactInfo;
    private Boolean done;
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    public CommunicationRequest(LocalDateTime date, User user, String contactInfo, Boolean done, Volunteer volunteer) {
        this.date = date;
        this.user = user;
        this.contactInfo = contactInfo;
        this.done = done;
        this.volunteer = volunteer;
    }

    public CommunicationRequest() {
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunicationRequest)) return false;
        CommunicationRequest that = (CommunicationRequest) o;
        return getId() == that.getId() && Objects.equals(getDate(), that.getDate()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getContactInfo(), that.getContactInfo()) && Objects.equals(getDone(), that.getDone()) && Objects.equals(getVolunteer(), that.getVolunteer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getUser(), getContactInfo(), getDone(), getVolunteer());
    }

    @Override
    public String toString() {
        return "CommunicationRequest{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + user +
                ", contactInfo='" + contactInfo + '\'' +
                ", done=" + done +
                ", volunteer=" + volunteer +
                '}';
    }
}
