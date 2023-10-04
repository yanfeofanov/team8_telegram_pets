package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.exception.DailyReportNullPointerException;
import pro.sky.telegrambot.exception.PetOwnerNullPointerException;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.DailyReportRepository;
import pro.sky.telegrambot.repository.PetOwnerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class DailyReportServiceTest {

    @Mock
    private DailyReportRepository dailyReportRepositoryMock;

    @InjectMocks
    private DailyReportService out;

    @Mock
    private PetOwnerService petOwnerServiceMock;

    static final LocalDate nowLocalDate = LocalDate.now(TimeZone.getTimeZone("GMT+3").toZoneId());

    static final String date = "2023-10-01";
    static final LocalDate currentDate = LocalDate.parse(date);
    static final LocalDateTime localDateTime = currentDate.atStartOfDay();
    static final LocalDateTime endDateTime = currentDate.plusDays(1).atStartOfDay();
    static final LocalDateTime endProbation = currentDate.plusDays(30).atStartOfDay();
    static final boolean probation = true;
    static final boolean leave = false;
    static final boolean checked = false;
    static final boolean checkedTrue = true;
    static final Shelter shelter = new Shelter("Приют собак", TypeOfPet.DOG);
    static final User userPetOwner = new User(1L,"user",222L,localDateTime);
    static final User userVolunteer = new User(1L,"user",123L,localDateTime);
    static final Volunteer volunteer = new Volunteer("V","Vm","89009009090",userVolunteer);
    static final PetOwner petOwner = new PetOwner("D","M","89009009009","test@mail.com",
            userPetOwner,volunteer,probation,endProbation);

    static final Pet pet = new Pet(1,TypeOfPet.DOG.toString(),"Rex",(byte) 1, "Ovcharka", shelter, petOwner, leave);
    static final DailyReport dailyReport = new DailyReport(localDateTime, petOwner, pet,
            "Это моя собака, с ней все хорошо",checked,volunteer);

    static final DailyReport dailyReportTrue = new DailyReport(localDateTime, petOwner, pet,
            "Это моя собака, с ней все хорошо",checkedTrue,volunteer);


    @Test
    void findReportByUserIdTest() {
        when(petOwnerServiceMock.findPetOwnerWithProbationaryPeriod(anyLong())).thenReturn(petOwner);
        when(out.getTodayDailyReportByPetOwner(petOwner,nowLocalDate)).thenReturn(dailyReport);
        assertThat(out.findReportByUserId(anyLong()))
                .isEqualTo(dailyReport);
    }
    @Test
    void getTodayDailyReportByPetOwnerTest() {
        when(dailyReportRepositoryMock.findByPetOwnerAndDateBetween(petOwner,localDateTime,endDateTime)).thenReturn(dailyReport);
        assertThat(out.getTodayDailyReportByPetOwner(petOwner,currentDate))
                .isNotNull()
                .isEqualTo(dailyReport);
    }

    @Test
    void getCheckedDailyReportTest() {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(dailyReportRepositoryMock.findDailyReportByChecked(false)).thenReturn(dailyReportCollection);
        assertThat(out.getCheckedDailyReport(false))
                .isNotNull()
                .isEqualTo(dailyReportCollection);
    }

    @Test
    void getAllDailyReportByPetOwnerTest() {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(petOwnerServiceMock.findPetOwner(anyInt())).thenReturn(petOwner);
        when(dailyReportRepositoryMock.findDailyReportByPetOwner(petOwner)).thenReturn(dailyReportCollection);
        assertThat(out.getAllDailyReportByPetOwner(anyInt()))
                .isNotNull()
                .isEqualTo(dailyReportCollection);
    }

    @Test
    void getAllDailyReportByPetOwnerNegativeTest() {
        when(petOwnerServiceMock.findPetOwner(anyInt())).thenReturn(null);
        Throwable throwable = catchThrowable(()->out.getAllDailyReportByPetOwner(anyInt()));
        assertThat(throwable).isInstanceOf(PetOwnerNullPointerException.class);
    }

    @Test
    void getDailyReportByIdTest() {
        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(dailyReport);
        assertThat(out.getDailyReportById(anyLong()))
                .isNotNull()
                .isEqualTo(dailyReport);
    }

    @Test
    void getDailyReportByIdNegativeTest() {
        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(null);
        Throwable throwable = catchThrowable(() -> out.getDailyReportById(anyLong()));
        assertThat(throwable).isInstanceOf(DailyReportNullPointerException.class);
    }


    @Test
    void checkDailyReportTest() {
        String expected = "статус отчета изменен";
        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(dailyReport);
        when(dailyReportRepositoryMock.save(dailyReport)).thenReturn(dailyReport);
        assertThat(out.checkDailyReport(anyLong(),true))
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void checkDailyReportIfAlreadyCheckTest() {
        String expected = "отчет уже проверен";
        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(dailyReportTrue);
        assertThat(out.checkDailyReport(anyLong(),true))
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void checkDailyReportNegativeTest() {
        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(null);
        Throwable throwable = catchThrowable(() -> out.getDailyReportById(anyLong()));
        assertThat(throwable).isInstanceOf(DailyReportNullPointerException.class);
    }

    @Test
    void getAllDailyReportByDateTest() {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(dailyReportRepositoryMock.findDailyReportByDateBetween(localDateTime,endDateTime)).thenReturn(dailyReportCollection);
        assertThat(out.getAllDailyReportByDate(date))
                .isNotNull()
                .isEqualTo(dailyReportCollection);
    }
}
