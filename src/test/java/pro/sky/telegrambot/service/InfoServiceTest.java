package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.exception.InfoNotFoundException;
import pro.sky.telegrambot.exception.ShelterNotFoundException;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {

    private final InfoRepository infoRepositoryMock = mock(InfoRepository.class);
    private final InfoService out = new InfoService(infoRepositoryMock);

    private final Shelter shelter1 = new Shelter(1,"cat", TypeOfPet.CAT);
    private final Info info1 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, "текст1", new Shelter());
    private final Info info2 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, "текст2", new Shelter());
    private final Info info4 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, "текст2", new Shelter(1,"cat", TypeOfPet.CAT));
    private final Info info3 = new Info();

    @Test
    void addInfoTest() {
        when(infoRepositoryMock.save(any(Info.class))).thenReturn(info1);
        assertThat(out.addInfo(info1))
                .isNotNull()
                .isEqualTo(info1);
        verify(infoRepositoryMock, new Times(1)).save(any(Info.class));
    }

    @Test
    void findByIdInfoPositiveTest() {
        Collection<Info> info3 = List.of(info1, info2);
        when(infoRepositoryMock.findByShelterId(anyInt())).thenReturn(info3);
        assertThat(out.findByShelterIdInfo(anyInt()))
                .isNotNull()
                .isEqualTo(info3);
        verify(infoRepositoryMock, new Times(2)).findByShelterId(anyInt());
    }

    @Test
    void findByIdInfoNegativeTest() {
        when(infoRepositoryMock.existsById(anyInt())).thenReturn(false);
        Throwable exception = assertThrows(ShelterNotFoundException.class, () -> {
            out.findByShelterIdInfo(info3.getId());
        });
        assertEquals("Информация с " + info3.getId() + " не найдена!", exception.getMessage());
    }


    @Test
    void updateInfoPositiveTest() {
        when(infoRepositoryMock.existsById(anyInt())).thenReturn(true);
        when(infoRepositoryMock.save(any(Info.class))).thenReturn(info1);
        assertThat(out.updateInfo(info1))
                .isNotNull()
                .isEqualTo(info1);
        verify(infoRepositoryMock, new Times(1)).existsById(anyInt());
        verify(infoRepositoryMock, new Times(1)).save(any(Info.class));
    }


    @Test
    void updateInfoNegativeTest() {
        when(infoRepositoryMock.existsById(anyInt())).thenReturn(false);
        Throwable exception = assertThrows(InfoNotFoundException.class, () -> {
            out.updateInfo(info1);
        });
        assertEquals("Информация с " + info1.getId() + " не найдена!", exception.getMessage());
    }

    @Test
    void findByTypeTest() {
        when(infoRepositoryMock.findByType(any(TypesOfInformation.class))).thenReturn(info4);
        assertThat(out.findByType(TypesOfInformation.LONG_INFO_ABOUT_SHELTER))
                .isNotNull()
                .isEqualTo(info4);
        verify(infoRepositoryMock, new Times(1)).findByType(TypesOfInformation.LONG_INFO_ABOUT_SHELTER);
    }

    @Test
    void findByTypeAndShelterTest() {
        when(infoRepositoryMock.findByTypeAndShelter(any(TypesOfInformation.class), any(Shelter.class))).thenReturn(info4);
        assertThat(out.findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, shelter1))
                .isNotNull()
                .isEqualTo(info4);
        verify(infoRepositoryMock, new Times(1)).findByTypeAndShelter(TypesOfInformation.LONG_INFO_ABOUT_SHELTER,new Shelter(1,"cat",TypeOfPet.CAT));

    }

}
