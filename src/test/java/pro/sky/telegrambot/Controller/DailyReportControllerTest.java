package pro.sky.telegrambot.Controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.controller.DailyReportController;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.*;
import pro.sky.telegrambot.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DailyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyReportRepository dailyReportRepositoryMock;

    @SpyBean
    private DailyReportService dailyReportService;

    @InjectMocks
    private DailyReportControllerTest dailyReportController;


    static final String date = "2023-01-23";
    static final LocalDate currentDate = LocalDate.parse(date);
    static final LocalDateTime localDateTime = currentDate.atStartOfDay();
    static final LocalDateTime endDateTime = currentDate.plusDays(1).atStartOfDay();
    static final LocalDateTime endProbation = currentDate.plusDays(30).atStartOfDay();
    static final boolean probation = true;
    static final boolean leave = false;
    static final boolean checked = false;
    static final User userPetOwner = new User(1L,"user",222L,localDateTime);
    static final User userVolunteer = new User(1L,"user",123L,localDateTime);
    static final Volunteer volunteer = new Volunteer("V","Vm","89009009090",userVolunteer);
    static final PetOwner petOwner = new PetOwner("D","M","89009009009","test@mail.com",
            userPetOwner,volunteer,probation,endProbation);
    static final Shelter shelter = new Shelter("Приют собак", TypeOfPet.DOG,null);

    static final Pet pet = new Pet("DOG","Rex",(byte) 1, "Ovcharka", shelter, petOwner, null, leave);
    static final DailyReport dailyReport = new DailyReport(localDateTime, petOwner, pet, null,
            "Это моя собака, с ней все хорошо",checked,volunteer);

    static final JSONObject dailyReportJson = new JSONObject();

    static {
        try {
            dailyReportJson.put("id",1L);
            dailyReportJson.put("date",dailyReport.getDate());
            dailyReportJson.put("petOwner",dailyReport.getPetOwner());
            dailyReportJson.put("pet",dailyReport.getPet());
            dailyReportJson.put("photo",dailyReport.getPhoto());
            dailyReportJson.put("reportBody",dailyReport.getReportBody());
            dailyReportJson.put("checked",dailyReport.getChecked());
            dailyReportJson.put("volunteer",dailyReport.getInspector());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getDailyReportByIdTest() throws Exception {
        DailyReport report = dailyReport;

        when(dailyReportRepositoryMock.findDailyReportById(anyLong())).thenReturn(report);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/dailyReport")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dailyReport.getId()))
                .andExpect(jsonPath("$.date").value(dailyReport.getDate()))
                .andExpect(jsonPath("$.petOwner").value(dailyReport.getPetOwner()))
                .andExpect(jsonPath("$.pet").value(dailyReport.getPet()))
                .andExpect(jsonPath("$.photo").value(dailyReport.getPhoto()))
                .andExpect(jsonPath("$.checked").value(dailyReport.getChecked()))
                .andExpect(jsonPath("$.volunteer").value(dailyReport.getInspector()));

    }
}
