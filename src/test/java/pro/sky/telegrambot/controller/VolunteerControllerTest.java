package pro.sky.telegrambot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;
import pro.sky.telegrambot.service.VolunteerService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = VolunteerController.class)
class VolunteerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VolunteerRepository volunteerRepository;

    @SpyBean
    private VolunteerService volunteerService;
    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addVolunteerTest() throws Exception {

        Volunteer newVolunteer = generateVolunteer();
        Volunteer newVolunteer2 = generateVolunteer();
        when(volunteerRepository.findAll()).thenReturn(List.of(newVolunteer2));
        when(volunteerRepository.save(ArgumentMatchers.any())).thenReturn(newVolunteer);
        String volunteerJson = objectMapper.writeValueAsString(newVolunteer);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/volunteer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(volunteerJson)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(newVolunteer.getName()))
                .andExpect(jsonPath("$.surname").value(newVolunteer.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(newVolunteer.getPhoneNumber()))
                .andExpect(jsonPath("$.user.userName").value(newVolunteer.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(newVolunteer.getUser().getChatId()));

        verify(volunteerService).addVolunteer(newVolunteer);
        verify(volunteerService, times(1)).addVolunteer(any());
    }

    @Test
    public void deleteVolunteerTest() throws Exception {
        Volunteer deleteVolunteer = generateVolunteer();
        deleteVolunteer.setId(11);
        when(volunteerRepository.findById(11)).thenReturn(Optional.of(deleteVolunteer));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/volunteer/11")
                                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(deleteVolunteer.getName()))
                .andExpect(jsonPath("$.surname").value(deleteVolunteer.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(deleteVolunteer.getPhoneNumber()))
                .andExpect(jsonPath("$.user.userName").value(deleteVolunteer.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(deleteVolunteer.getUser().getChatId()));

        verify(volunteerRepository).findById(11);
        verify(volunteerService, times(1)).deleteVolunteer(anyInt());
        Mockito.reset(volunteerRepository);

        when(volunteerRepository.findById(eq(2))).thenReturn(Optional.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/volunteer/1")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(volunteerRepository, never()).delete(deleteVolunteer);

    }


    @Test
    public void getVolunteerTest() throws Exception {
        Volunteer getVolunteer = generateVolunteer();
        getVolunteer.setPhoneNumber("89778861214");
        when(volunteerRepository.findVolunteerByPhoneNumber("89778861214")).thenReturn(getVolunteer);


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/volunteer/89778861214")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(getVolunteer.getName()))
                .andExpect(jsonPath("$.surname").value(getVolunteer.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(getVolunteer.getPhoneNumber()))
                .andExpect(jsonPath("$.user.userName").value(getVolunteer.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(getVolunteer.getUser().getChatId()));

        verify(volunteerRepository).findVolunteerByPhoneNumber("89778861214");
        verify(volunteerRepository, times(1)).findVolunteerByPhoneNumber(anyString());
        Mockito.reset(volunteerRepository);

        when(volunteerRepository.findVolunteerByPhoneNumber("89778861214")).thenThrow(InvalidInputDataException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/volunteer/89778861214")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        Mockito.reset(volunteerRepository);

        when(volunteerRepository.findVolunteerByPhoneNumber(" ")).thenThrow(InvalidInputDataException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/volunteer/ ")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
    }

    @Test
    public void getAllVolunteers() throws Exception{
        List<Volunteer> volunteers = new ArrayList<>();
        Volunteer volunteer1 = generateVolunteer();
        Volunteer volunteer2 = generateVolunteer();
        Volunteer volunteer3 = generateVolunteer();
        volunteers.add(volunteer1);
        volunteers.add(volunteer2);
        volunteers.add(volunteer3);
        when(volunteerRepository.findAll()).thenReturn(volunteers);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/volunteer/all")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value(volunteer1.getName()))
                .andExpect(jsonPath("$[1].surname").value(volunteer2.getSurname()))
                .andExpect(jsonPath("$[2].user.chatId").value(volunteer3.getUser().getChatId()))
                .andExpect(result -> {
                            List<Volunteer> volunteerList = objectMapper.readValue(
                                    result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    }
                            );
                            assertThat(volunteerList)
                                    .isNotNull();
                            assertThat(volunteerList.size()).isEqualTo(3);
                        });

        verify(volunteerRepository).findAll();
        verify(volunteerRepository, times(1)).findAll();
    }

    private Volunteer generateVolunteer() {
        Volunteer volunteer = new Volunteer();
        volunteer.getId();
        volunteer.setName(faker.name().firstName());
        volunteer.setSurname(faker.name().lastName());
        volunteer.setPhoneNumber(faker.phoneNumber().phoneNumber());
        User user = new User();
        user.setUserName(faker.name().username());
        user.setId(generateId());
        user.setChatId(user.getId());
        long days = faker.number().numberBetween(1, 30);
        long hours = faker.number().numberBetween(1, 5);
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(days).minusHours(hours);
        user.setDateAdded(localDateTime);
        volunteer.setUser(user);
        return volunteer;

    }

    private long generateId() {
        int numberOfDigits = 8;
        long min = (long) Math.pow(10, numberOfDigits - 1);
        long max = (long) Math.pow(10, numberOfDigits) - 1;
        Random random = new Random();
        long randomLong = min + random.nextLong() % (max - min + 1);
        return randomLong;
    }
}