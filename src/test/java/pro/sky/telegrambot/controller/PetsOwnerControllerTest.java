package pro.sky.telegrambot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.PetOwnerRepository;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.service.PetOwnerService;
import pro.sky.telegrambot.service.VolunteerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PetsOwnerController.class)
class PetsOwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetOwnerRepository petOwnerRepository;
    @MockBean
    private PetRepository petRepository;
    @SpyBean
    private PetOwnerService petOwnerService;
    @MockBean
    private TelegramBot telegramBot;

    private final Faker faker = new Faker();
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void findPetOwnerTest() throws Exception {
        PetOwner getPetOwner = generatePetOwner();
        getPetOwner.setPhoneNumber("89778861214");
        when(petOwnerRepository.findPetOwnerByPhoneNumber("89778861214")).thenReturn(getPetOwner);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pet_owner/89778861214")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(getPetOwner.getName()))
                .andExpect(jsonPath("$.surname").value(getPetOwner.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(getPetOwner.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(getPetOwner.getEmail()))
                .andExpect(jsonPath("$.probation").value(true))
                .andExpect(jsonPath("$.endProbation").value(getPetOwner.getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.user.userName").value(getPetOwner.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(getPetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.user.chatId").value(getPetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.volunteer.name").value(getPetOwner.getVolunteer().getName()))
                .andExpect(jsonPath("$.volunteer.phoneNumber").value(getPetOwner.getVolunteer().getPhoneNumber()));

        verify(petOwnerRepository).findPetOwnerByPhoneNumber("89778861214");
        verify(petOwnerRepository, times(1)).findPetOwnerByPhoneNumber(anyString());
        Mockito.reset(petOwnerRepository);

        when(petOwnerRepository.findPetOwnerByPhoneNumber("89778861214")).thenThrow(InvalidInputDataException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pet_owner/89778861214")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(petOwnerRepository, times(1)).findPetOwnerByPhoneNumber(anyString());
        Mockito.reset(petOwnerRepository);

        when(petOwnerRepository.findPetOwnerByPhoneNumber(" ")).thenThrow(InvalidInputDataException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pet_owner/ ")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });

    }

   @Test
    public void addPetOwnerTest() throws Exception {
        PetOwner newPetOwner = generatePetOwner();
        when(petOwnerRepository.save(any(PetOwner.class))).thenReturn(newPetOwner);
        String petOwnerJson = objectMapper.writeValueAsString(newPetOwner);


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pet_owner")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(petOwnerJson)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(newPetOwner.getName()))
                .andExpect(jsonPath("$.surname").value(newPetOwner.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(newPetOwner.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(newPetOwner.getEmail()))
                .andExpect(jsonPath("$.probation").value(true))
                .andExpect(jsonPath("$.endProbation").value(newPetOwner.getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.user.userName").value(newPetOwner.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(newPetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.user.chatId").value(newPetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.volunteer.name").value(newPetOwner.getVolunteer().getName()))
                .andExpect(jsonPath("$.volunteer.phoneNumber").value(newPetOwner.getVolunteer().getPhoneNumber()));

        verify(petOwnerRepository).save(newPetOwner);
        verify(petOwnerRepository, times(1)).save(any(PetOwner.class));
        Mockito.reset(petOwnerRepository);

    }

    @Test
    public void deletePetOwner() throws Exception {
        PetOwner deletePetOwner = generatePetOwner();
        deletePetOwner.setId(11);
        when(petOwnerRepository.findById(11)).thenReturn(Optional.of(deletePetOwner));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/pet_owner/11")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value(deletePetOwner.getName()))
                .andExpect(jsonPath("$.surname").value(deletePetOwner.getSurname()))
                .andExpect(jsonPath("$.phoneNumber").value(deletePetOwner.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(deletePetOwner.getEmail()))
                .andExpect(jsonPath("$.probation").value(true))
                .andExpect(jsonPath("$.endProbation").value(deletePetOwner.getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.user.userName").value(deletePetOwner.getUser().getUserName()))
                .andExpect(jsonPath("$.user.chatId").value(deletePetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.user.chatId").value(deletePetOwner.getUser().getChatId()))
                .andExpect(jsonPath("$.volunteer.name").value(deletePetOwner.getVolunteer().getName()))
                .andExpect(jsonPath("$.volunteer.phoneNumber").value(deletePetOwner.getVolunteer().getPhoneNumber()));

        verify(petOwnerRepository).findById(11);
        verify(petOwnerRepository, times(1)).findById(anyInt());
        Mockito.reset(petOwnerRepository);

        when(petOwnerRepository.findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/pet_owner/2")
        )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(petOwnerRepository).findById(2);
        verify(petOwnerRepository, never()).delete(any(PetOwner.class));
    }

    /*@Test
    public void getCatOwnersTest() throws Exception {
        List<PetOwner> petOwners = generateList();
        when(petRepository.findAll()).thenReturn(petOwners);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pet_owner/all/cat_owners")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value(petOwners.get(0).getName()))
                .andExpect(jsonPath("$[1].surname").value(petOwners.get(1).getSurname()))
                .andExpect(jsonPath("$[2].user.chatId").value(petOwners.get(2).getUser().getId()))
                .andExpect(jsonPath("$[2].volunteer.phoneNumber").value(petOwners.get(2).getVolunteer().getPhoneNumber()))
                .andExpect(jsonPath("$[0].endProbation").value(petOwners.get(0).getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(result -> {
                    List<PetOwner> petOwnerList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(petOwnerList)
                            .isNotNull();
                });

        verify(petOwnerService).getCatOwners();
        verify(petOwnerService, times(1)).getCatOwners();
        verify(petOwnerService, never()).getDogOwners();
    }*/

   /* @Test
    public void getDogOwnersTest() throws Exception {
        List<PetOwner> petOwners = generateList();
        when(petOwnerService.getDogOwners()).thenReturn(petOwners);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pet_owner/all/dog_owners")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value(petOwners.get(0).getName()))
                .andExpect(jsonPath("$[1].surname").value(petOwners.get(1).getSurname()))
                .andExpect(jsonPath("$[2].user.chatId").value(petOwners.get(2).getUser().getId()))
                .andExpect(jsonPath("$[2].volunteer.phoneNumber").value(petOwners.get(2).getVolunteer().getPhoneNumber()))
                .andExpect(jsonPath("$[0].endProbation").value(petOwners.get(0).getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(result -> {
                    List<PetOwner> petOwnerList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(petOwnerList)
                            .isNotNull();
                });

        verify(petOwnerService).getDogOwners();
        verify(petOwnerService, times(1)).getDogOwners();
        verify(petOwnerService, never()).getCatOwners();
    }
*/

    @Test
    public void getProbationPetOwnersTest() throws Exception {
        List<PetOwner> petOwners = generateList();
        petOwners.get(3).setProbation(false);
        when(petOwnerRepository.findAll()).thenReturn(petOwners);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/pet_owner/probation")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value(petOwners.get(0).getName()))
                .andExpect(jsonPath("$[1].surname").value(petOwners.get(1).getSurname()))
                .andExpect(jsonPath("$[2].user.chatId").value(petOwners.get(2).getUser().getId()))
                .andExpect(jsonPath("$[2].volunteer.phoneNumber").value(petOwners.get(2).getVolunteer().getPhoneNumber()))
                .andExpect(jsonPath("$[0].endProbation").value(petOwners.get(0).getEndProbation().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(result -> {
                    List<PetOwner> petOwnerList = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(petOwnerList)
                            .isNotNull();
                    assertThat(petOwnerList.size()).isEqualTo(3);
                });

        verify(petOwnerService).getPetOwnersOnProbation();
        verify(petOwnerService, times(1)).getPetOwnersOnProbation();
    }


    @Test
    public void changeStatusProbationTest() {
    }

    private PetOwner generatePetOwner() {
        PetOwner petOwner = new PetOwner();
        petOwner.getId();
        petOwner.setName(faker.name().firstName());
        petOwner.setSurname(faker.name().lastName());
        petOwner.setPhoneNumber(faker.phoneNumber().phoneNumber());
        petOwner.setEmail(faker.internet().emailAddress());
        petOwner.setProbation(true);
        User user = new User();
        user.setUserName(faker.name().username());
        user.setId(generateId());
        user.setChatId(user.getId());
        long days = faker.number().numberBetween(1, 30);
        long hours = faker.number().numberBetween(1, 5);
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(days).minusHours(hours);
        user.setDateAdded(localDateTime);
        petOwner.setEndProbation(localDateTime.plusDays(30));
        petOwner.setUser(user);
        Volunteer volunteer = generateVolunteer();
        petOwner.setVolunteer(volunteer);
        return petOwner;

    }

    private long generateId() {
        int numberOfDigits = 8;
        long min = (long) Math.pow(10, numberOfDigits - 1);
        long max = (long) Math.pow(10, numberOfDigits) - 1;
        Random random = new Random();
        long randomLong = min + random.nextLong() % (max - min + 1);
        return randomLong;
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

    private List<PetOwner> generateList() {
        PetOwner petOwner1 = generatePetOwner();
        PetOwner petOwner2 = generatePetOwner();
        PetOwner petOwner3 = generatePetOwner();
        PetOwner petOwner4 = generatePetOwner();
        List<PetOwner> petOwners = List.of(petOwner1, petOwner2, petOwner3, petOwner4);
        return petOwners;
    }

}