package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Photo;
import pro.sky.telegrambot.repository.PhotoRepository;

@Service
public class PhotoService {

    private final PhotoRepository photoPetRepository;

    public PhotoService(PhotoRepository photoPetRepository) {
        this.photoPetRepository = photoPetRepository;
    }

    public Photo savePhotoReport(Photo photoPet) {
        return photoPetRepository.save(photoPet);
    }
}

