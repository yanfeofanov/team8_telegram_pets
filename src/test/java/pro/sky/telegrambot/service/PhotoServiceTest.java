package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.Photo;
import pro.sky.telegrambot.repository.PhotoRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    private final PhotoRepository photoRepositoryMock = mock(PhotoRepository.class);
    private final PhotoService out = new PhotoService(photoRepositoryMock);
    private static final Photo testPhoto = new Photo();
    @Test
    void savePhotoReport() {
        when(photoRepositoryMock.save(any(Photo.class))).thenReturn(testPhoto);
        assertThat(out.savePhotoReport(testPhoto))
                .isNotNull()
                .isEqualTo(testPhoto);
        verify(photoRepositoryMock, new Times(1)).save(any(Photo.class));
    }
}