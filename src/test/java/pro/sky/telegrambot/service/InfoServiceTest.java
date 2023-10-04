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
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.InfoRepository;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.checkerframework.checker.nullness.Opt.orElseThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {

    private final InfoRepository infoRepositoryMock = mock(InfoRepository.class);
    private final InfoService out = new InfoService(infoRepositoryMock);

    private final Info info1 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER,"текст1",new Shelter());
    private final Info info2 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER,"текст2",new Shelter());

    @Test
    void addInfoTest(){
        when(infoRepositoryMock.save(any(Info.class))).thenReturn(info1);
        assertThat(out.addInfo(info1))
                .isNotNull()
                .isEqualTo(info1);
        verify(infoRepositoryMock,new Times(1)).save(any(Info.class));
    }

    @Test
    void findByIdInfoTest(){
        Collection<Info> info3 = List.of(info1,info2);
        when(infoRepositoryMock.findByShelterId(anyInt())).thenReturn(info3);
        assertThat(out.findByShelterIdInfo(anyInt()))
                .isNotNull()
                .isEqualTo(info3);
        verify(infoRepositoryMock, new Times(2)).findByShelterId(anyInt());
    }

//    @Test
//    void updateInfoShelterTest(){
//        Exception exception = assertThrow(BadParamException.class )
//        Collection<Info> info3 = List.of(info1,info2);
//        when(infoRepositoryMock.findByShelterId(anyInt())).thenReturn(info3);
//        assertThat(out.updateInfoShelter(anyInt(),info2))
//                .isNotNull()
//                        .isEqualTo(info3)
//                                .orElseThrow(BadParamException);
//        verify(infoRepositoryMock, new Times(1)).findById(anyInt());
//    }
}
