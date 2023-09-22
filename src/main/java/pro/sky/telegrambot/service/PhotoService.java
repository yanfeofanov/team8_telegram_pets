package pro.sky.telegrambot.service;

        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;
        import pro.sky.telegrambot.model.Pet;
        import pro.sky.telegrambot.model.Photo;
        import pro.sky.telegrambot.repository.PhotoRepository;

        import java.io.*;
        import java.nio.file.Files;
        import java.nio.file.Path;

        import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class PhotoService {

    @Value("path.to.photo.dir")
    private String coversDir;

    private final PetService petService;
    private final PhotoRepository photoPetRepository;

    public PhotoService(PetService petService, PhotoRepository photoPetRepository) {
        this.petService = petService;
        this.photoPetRepository = photoPetRepository;
    }
    public void uploadPhoto(Long petId, MultipartFile file) throws IOException {
        Pet pet = petService.findPet(petId);

        Path filePath = Path.of(coversDir, petId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Photo photo = findPhoto(petId);
        photoPetRepository.save(photo);
        //photo.setPet(pet);
        photo.setFilePath(filePath.toString());
        photo.setFileSize(file.getSize());
        photo.setMediaType(file.getContentType());

        photoPetRepository.save(photo);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Photo findPhoto(Long Id) {
        return photoPetRepository.findPhotoById(Id).orElse(new Photo());
    }

    public Photo savePhotoReport (Photo photoPet) {
        return photoPetRepository.save(photoPet);
    }
}

