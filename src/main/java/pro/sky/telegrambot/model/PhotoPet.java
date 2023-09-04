package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class PhotoPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    private String mediaType;

    private long fileSize;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public PhotoPet() {
    }

    public PhotoPet(Long id, String filePath, String mediaType, long fileSize, byte[] data) {
        this.id = id;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (o == null || getClass() != o.getClass()) return false;
        PhotoPet photoPet = (PhotoPet) o;
        return Objects.equals(id, photoPet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

