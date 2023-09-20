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
    private LocalDateTime date;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "media_type")
    private String mediaType;
    @Column(name = "file_size")
    private long fileSize;
    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Photo() {
    }

    public Photo(LocalDateTime date, String filePath, String mediaType, long fileSize, byte[] data) {
        this.date = date;
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
        return getFileSize() == photo.getFileSize() && Objects.equals(getId(), photo.getId()) && Objects.equals(getDate(), photo.getDate()) && Objects.equals(getFilePath(), photo.getFilePath()) && Objects.equals(getMediaType(), photo.getMediaType()) && Arrays.equals(getData(), photo.getData());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getDate(), getFilePath(), getMediaType(), getFileSize());
        result = 31 * result + Arrays.hashCode(getData());
        return result;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", date=" + date +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", fileSize=" + fileSize +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

