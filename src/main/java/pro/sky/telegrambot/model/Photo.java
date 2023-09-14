package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "report_id")
    private DailyReport dailyReport;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "media_type")
    private String mediaType;
    @Column(name = "file_size")
    private long fileSize;
    @Lob
    private byte[] data;

    public Photo() {
    }

    public Photo(Pet pet, LocalDateTime date, DailyReport dailyReport, String filePath, String mediaType, long fileSize, byte[] data) {
        this.pet = pet;
        this.date = date;
        this.dailyReport = dailyReport;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public DailyReport getDailyReport() {
        return dailyReport;
    }

    public void setDailyReport(DailyReport dailyReport) {
        this.dailyReport = dailyReport;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        Photo photo = (Photo) o;
        return getFileSize() == photo.getFileSize() && Objects.equals(getId(), photo.getId()) && Objects.equals(getPet(), photo.getPet()) && Objects.equals(getDate(), photo.getDate()) && Objects.equals(getDailyReport(), photo.getDailyReport()) && Objects.equals(getFilePath(), photo.getFilePath()) && Objects.equals(getMediaType(), photo.getMediaType()) && Arrays.equals(getData(), photo.getData());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getPet(), getDate(), getDailyReport(), getFilePath(), getMediaType(), getFileSize());
        result = 31 * result + Arrays.hashCode(getData());
        return result;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", pet=" + pet +
                ", date=" + date +
                ", dailyReport=" + dailyReport +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", fileSize=" + fileSize +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

