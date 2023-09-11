package pro.sky.telegrambot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegrambot.service.PhotoService;

import java.io.IOException;

@RestController
@RequestMapping("photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile photoPet) throws IOException {
        return;
    }
}
