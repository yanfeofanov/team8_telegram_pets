package pro.sky.telegrambot.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.controller.DailyReportController;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyReportController.class)
public class DailyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyReportService dailyReportServiceMock;

    static final String date = "2023-09-25";
    static final LocalDate currentDate = LocalDate.parse(date);
    static final LocalDateTime localDateTime = currentDate.atStartOfDay();
    static final LocalDateTime endDateTime = currentDate.plusDays(1).atStartOfDay();
    static final LocalDateTime endProbation = currentDate.plusDays(30).atStartOfDay();
    static final boolean probation = true;
    static final boolean leave = false;
    static final boolean checked = false;
    static final Shelter shelter = new Shelter("Приют собак", TypeOfPet.DOG);
    static final User userPetOwner = new User(1L,"user",222L,localDateTime);
    static final User userVolunteer = new User(1L,"user",123L,localDateTime);
    static final Volunteer volunteer = new Volunteer("V","Vm","89009009090",userVolunteer);
    static final PetOwner petOwner = new PetOwner("D","M","89009009009","test@mail.com",
            userPetOwner,volunteer,probation,endProbation);

    static final Pet pet = new Pet(1,TypeOfPet.DOG.toString(),"Rex",(byte) 1, "Ovcharka", shelter, petOwner, leave);
    static final DailyReport dailyReport = new DailyReport(localDateTime, petOwner, pet,
            "Это моя собака, с ней все хорошо",checked,volunteer);

    static final JSONObject shelterJson = new JSONObject();
    static final JSONObject userPetOwnerJson = new JSONObject();
    static final JSONObject userVolunteerJson = new JSONObject();
    static final JSONObject volunteerJson = new JSONObject();
    static final JSONObject petOwnerJson = new JSONObject();
    static final JSONObject petJson = new JSONObject();
    static final JSONObject dailyReportJson = new JSONObject();

    static {
        try {
            shelterJson.put("id", shelter.getId());
            shelterJson.put("type", shelter.getType());
            shelterJson.put("name", shelter.getName());

            userPetOwnerJson.put("id", userPetOwner.getId());
            userPetOwnerJson.put("userName", userPetOwner.getUserName());
            userPetOwnerJson.put("chatId", userPetOwner.getChatId());
            userPetOwnerJson.put("dateAdded", userPetOwner.getDateAdded());

            userVolunteerJson.put("id", userVolunteer.getId());
            userVolunteerJson.put("userName", userVolunteer.getUserName());
            userVolunteerJson.put("chatId", userVolunteer.getChatId());
            userVolunteerJson.put("dateAdded", userVolunteer.getDateAdded());

            volunteerJson.put("id", volunteer.getId());
            volunteerJson.put("name", volunteer.getName());
            volunteerJson.put("surname", volunteer.getSurname());
            volunteerJson.put("phoneNumber", volunteer.getPhoneNumber());
            volunteerJson.put("user", userVolunteerJson);

            petOwnerJson.put("id", petOwner.getId());
            petOwnerJson.put("name", petOwner.getName());
            petOwnerJson.put("surname", petOwner.getSurname());
            petOwnerJson.put("phoneNumber", petOwner.getPhoneNumber());
            petOwnerJson.put("email", petOwner.getEmail());
            petOwnerJson.put("user", userPetOwnerJson);
            petOwnerJson.put("volunteer", volunteerJson);
            petOwnerJson.put("probation", petOwner.isProbation());
            petOwnerJson.put("endProbation", petOwner.getEndProbation());

            petJson.put("id", pet.getId());
            petJson.put("type", pet.getType());
            petJson.put("age", pet.getAge());
            petJson.put("breed", pet.getBreed());
            petJson.put("shelter", shelterJson);
            petJson.put("petOwner", petOwnerJson);
            petJson.put("leave", pet.getLeave());

            dailyReportJson.put("id",dailyReport.getId());
            dailyReportJson.put("date",dailyReport.getDate());
            dailyReportJson.put("petOwner",petOwnerJson);
            dailyReportJson.put("pet",petJson);
            dailyReportJson.put("reportBody",dailyReport.getReportBody());
            dailyReportJson.put("checked",dailyReport.getChecked());
            dailyReportJson.put("volunteer",volunteerJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getDailyReportByIdTest() throws Exception {
        when(dailyReportServiceMock.getDailyReportById(anyLong())).thenReturn(dailyReport);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/dailyReport/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dailyReport.getId()))
                .andExpect(jsonPath("$.date").value(dailyReport.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.petOwner.id").value(dailyReport.getPetOwner().getId()))
                .andExpect(jsonPath("$.pet.id").value(dailyReport.getPet().getId()))
                .andExpect(jsonPath("$.checked").value(dailyReport.getChecked()))
                .andExpect(jsonPath("$.inspector.id").value(dailyReport.getInspector().getId()));
    }

    @Test
    void getCheckedOrNotReportTest() throws Exception {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(dailyReportServiceMock.getCheckedDailyReport(anyBoolean())).thenReturn(dailyReportCollection);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(dailyReport.getId()))
                .andExpect(jsonPath("$.[0].date").value(dailyReport.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.[0].petOwner.id").value(dailyReport.getPetOwner().getId()))
                .andExpect(jsonPath("$.[0].pet.id").value(dailyReport.getPet().getId()))
                .andExpect(jsonPath("$.[0].checked").value(dailyReport.getChecked()))
                .andExpect(jsonPath("$.[0].inspector.id").value(dailyReport.getInspector().getId()));
    }

    @Test
    void getAllDailyReportByOwnerIdTest() throws Exception {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(dailyReportServiceMock.getAllDailyReportByPetOwner(anyInt())).thenReturn(dailyReportCollection);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/owner/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(dailyReport.getId()))
                .andExpect(jsonPath("$.[0].date").value(dailyReport.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.[0].petOwner.id").value(dailyReport.getPetOwner().getId()))
                .andExpect(jsonPath("$.[0].pet.id").value(dailyReport.getPet().getId()))
                .andExpect(jsonPath("$.[0].checked").value(dailyReport.getChecked()))
                .andExpect(jsonPath("$.[0].inspector.id").value(dailyReport.getInspector().getId()));
    }

    @Test
    void getAllDailyReportByDateTest() throws Exception {
        Collection<DailyReport> dailyReportCollection = List.of(dailyReport);
        when(dailyReportServiceMock.getAllDailyReportByDate(date)).thenReturn(dailyReportCollection);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/date")
                        .param("date", date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(dailyReport.getId()))
                .andExpect(jsonPath("$.[0].date").value(dailyReport.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.[0].petOwner.id").value(dailyReport.getPetOwner().getId()))
                .andExpect(jsonPath("$.[0].pet.id").value(dailyReport.getPet().getId()))
                .andExpect(jsonPath("$.[0].checked").value(dailyReport.getChecked()))
                .andExpect(jsonPath("$.[0].inspector.id").value(dailyReport.getInspector().getId()));
    }


    @Test
    void checkedReportTest() {
        String expected = "статус отчета изменен";
        when(dailyReportServiceMock.checkDailyReport(1L, true)).thenReturn(expected);
        assertThat(dailyReportServiceMock.checkDailyReport(1L,true)).isEqualTo(expected);
    }
}
