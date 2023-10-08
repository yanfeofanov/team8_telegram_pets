package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "daily_report")
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
    @Column(name = "report_body")
    private String reportBody;
    private Boolean checked;
    private Boolean approved;
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer inspector;

    public DailyReport() {
    }

    public DailyReport(LocalDateTime date, PetOwner petOwner, Pet pet, Photo photo, String reportBody, Boolean checked, Boolean approved, Volunteer inspector) {
        this.date = date;
        this.petOwner = petOwner;
        this.pet = pet;
        this.photo = photo;
        this.reportBody = reportBody;
        this.checked = checked;
        this.inspector = inspector;
        this.approved = approved;
    }

    public DailyReport(Long id, LocalDateTime date, PetOwner petOwner, Pet pet, Photo photo, String reportBody, Boolean checked, Boolean approved, Volunteer inspector) {
        this.id = id;
        this.date = date;
        this.petOwner = petOwner;
        this.pet = pet;
        this.photo = photo;
        this.reportBody = reportBody;
        this.checked = checked;
        this.approved = approved;
        this.inspector = inspector;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Volunteer getInspector() {
        return inspector;
    }

    public void setInspector(Volunteer inspector) {
        this.inspector = inspector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyReport)) return false;
        DailyReport that = (DailyReport) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getPetOwner(), that.getPetOwner()) && Objects.equals(getPet(), that.getPet()) && Objects.equals(getPhoto(), that.getPhoto()) && Objects.equals(getReportBody(), that.getReportBody()) && Objects.equals(getChecked(), that.getChecked()) && Objects.equals(getApproved(), that.getApproved()) && Objects.equals(getInspector(), that.getInspector());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getPetOwner(), getPet(), getPhoto(), getReportBody(), getChecked(), getApproved(), getInspector());
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "id=" + id +
                ", date=" + date +
                ", petOwner=" + petOwner +
                ", pet=" + pet +
                ", photo=" + photo +
                ", reportBody='" + reportBody + '\'' +
                ", checked=" + checked +
                ", inspector=" + inspector +
                ", approved=" + approved +
                '}';
    }
}
