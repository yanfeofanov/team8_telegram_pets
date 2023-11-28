package pro.sky.telegrambot.service;

import com.github.javafaker.Faker;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.PetOwnerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetOwnerServiceTest {
    @Mock
    private PetOwnerRepository petOwnerRepository;
    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private PetOwnerService out;
    private final Faker faker = new Faker();
    private List<PetOwner> petOwners = new ArrayList<>();

    @BeforeEach
    void setUp() {
        PetOwner petOwner1 = generatePetOwner();
        PetOwner petOwner2 = generatePetOwner();
        PetOwner petOwner3 = generatePetOwner();
        petOwners.add(petOwner1);
        petOwners.add(petOwner2);
        petOwners.add(petOwner3);
    }

    @Test
    public void findPetOwnerByPhoneExceptionTest() {
        PetOwner petOwner = generatePetOwner();
        petOwner.setPhoneNumber(" ");
        assertThatExceptionOfType(InvalidInputDataException.class)
                .isThrownBy(() -> out.findPetOwnerByPhone(petOwner.getPhoneNumber()));
    }

    @Test
    public void addPetOwnerExceptionTest() {
        PetOwner petOwner = generatePetOwner();
        when(petOwnerRepository.findAll()).thenReturn(petOwners);
        when(petOwnerRepository.save(petOwner)).thenThrow(ErrorCollisionException.class);
        assertThatExceptionOfType(ErrorCollisionException.class)
                .isThrownBy(() -> out.addPetOwner(petOwner));
        petOwner.setUser(petOwner.getUser());
        assertThatExceptionOfType(ErrorCollisionException.class)
                .isThrownBy(() -> out.addPetOwner(petOwner));
    }

    @Test
    void checkProbation() {
        LocalDate localDate = LocalDate.now();
        ReflectionTestUtils.setField(out, "telegramBot", telegramBot);
        for (PetOwner petOwner : petOwners) {
            petOwner.setEndProbation(LocalDateTime.now());
        }
        when(petOwnerRepository.findAllByEndProbationIsToday(localDate)).thenReturn(petOwners);
        out.checkProbation();
        for (PetOwner petOwner : petOwners) {
            long chatId = petOwner.getVolunteer().getUser().getChatId();
            String expectedMessageText = "Необходимо принять решение о статусе следующих усыновителей ";
            InlineKeyboardMarkup expectedKeyboard = out.generateMenu(petOwners);
            SendMessage expectedMessage = new SendMessage(chatId, expectedMessageText);
            expectedMessage.replyMarkup(expectedKeyboard);
            assertThat(out.sendReply(chatId, expectedMessageText, expectedKeyboard))
                    .isEqualTo(telegramBot.execute(expectedMessage));

            verify(telegramBot, times(1)).execute(expectedMessage);
        }

        verify(petOwnerRepository, times(1)).findAllByEndProbationIsToday(localDate);


        }

    @Test
    void generateMenu() {
    }

    @Test
    void changeProbationStatusExceptionTest() {
        PetOwner petOwner = generatePetOwner();
        petOwner.setId(11);
        when(petOwnerRepository.findById(11)).thenReturn(Optional.of(petOwner));

        assertThatExceptionOfType(InvalidInputDataException.class)
                .isThrownBy(() -> out.changeProbationStatus(petOwner.getId(), 4));
    }

    @Test
    void sendReply() {
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