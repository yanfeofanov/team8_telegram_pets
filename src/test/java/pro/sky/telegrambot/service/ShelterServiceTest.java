package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.exception.ShelterNotFoundException;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.ShelterRepository;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {
    private final ShelterRepository shelterRepositoryMock = mock(ShelterRepository.class);
    private final ShelterService out = new ShelterService(shelterRepositoryMock);

    static private final Shelter testShelter = new Shelter("тестовый приют", TypeOfPet.CAT);

    @Test
    void findByTypeTest() {
        when(shelterRepositoryMock.findByType(any(TypeOfPet.class))).thenReturn(testShelter);
        assertThat(out.findByType(TypeOfPet.DOG))
                .isNotNull()
                .isEqualTo(testShelter);
        verify(shelterRepositoryMock, new Times(1)).findByType(any(TypeOfPet.class));
    }

    @Test
    void addShelterTest() {
        when(shelterRepositoryMock.save(any(Shelter.class))).thenReturn(testShelter);
        assertThat(out.addShelter(testShelter))
                .isNotNull()
                .isEqualTo(testShelter);
        verify(shelterRepositoryMock, new Times(1)).save(any(Shelter.class));
    }

    @Test
    void deleteShelterByIdTest() {
        out.deleteShelterById(testShelter.getId());
        verify(shelterRepositoryMock, new Times(1)).deleteById(anyInt());
    }

    @Test
    void updateShelterPositiveTest() {
        when(shelterRepositoryMock.existsById(anyInt())).thenReturn(true);
        when(shelterRepositoryMock.save(any(Shelter.class))).thenReturn(testShelter);
        assertThat(out.updateShelter(testShelter))
                .isNotNull()
                .isEqualTo(testShelter);
        verify(shelterRepositoryMock, new Times(1)).existsById(anyInt());
        verify(shelterRepositoryMock, new Times(1)).save(any(Shelter.class));
    }

    @Test
    void updateShelterNegativeTest() {
        when(shelterRepositoryMock.existsById(anyInt())).thenReturn(false);
        Throwable throwable = catchThrowable(() -> out.updateShelter(testShelter));
        assertThat(throwable).isInstanceOf(ShelterNotFoundException.class);
        verify(shelterRepositoryMock, new Times(1)).existsById(anyInt());
        verify(shelterRepositoryMock, new Times(0)).save(any());
    }
}