package pro.sky.telegrambot.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {
    @Mock
    private VolunteerRepository volunteerRepository;
    @InjectMocks
    private VolunteerService out;
    private final Faker faker = new Faker();
    private List<Volunteer> volunteers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Volunteer volunteer1 = generateVolunteer();
        Volunteer volunteer2 = generateVolunteer();
        Volunteer volunteer3 = generateVolunteer();
        volunteers.add(volunteer1);
        volunteers.add(volunteer2);
        volunteers.add(volunteer3);

    }


    @Test
    public void getRandomVolunteer() {
        ;
        when(volunteerRepository.findAll()).thenReturn(volunteers);
        assertThat(out.getRandomVolunteer()).isNotNull().isIn(volunteers);
    }

    @Test
    public void addVolunteerExceptionOfExistPhone() {
        when(volunteerRepository.findAllByPhoneNumber(volunteers.get(0).getPhoneNumber()))
                .thenReturn(List.of(volunteers.get(0)));
        Volunteer newVolunteer = generateVolunteer();
        newVolunteer.setPhoneNumber(volunteers.get(0).getPhoneNumber());
        assertThatExceptionOfType(ErrorCollisionException.class)
                .isThrownBy(() -> out.addVolunteer(newVolunteer));
    }

    @Test
    public void addVolunteerExceptionOfPhoneIsNull() {
        Volunteer newVolunteer = generateVolunteer();
        newVolunteer.setPhoneNumber(null);
        assertThatExceptionOfType(InvalidInputDataException.class)
                .isThrownBy(() -> out.addVolunteer(newVolunteer));

    }

    @Test
    public void addVolunteerExceptionIsExist() {
        when(volunteerRepository.findAll()).thenReturn(volunteers);
        Volunteer newVolunteer = volunteers.get(0);
        assertThatExceptionOfType(ErrorCollisionException.class)
                .isThrownBy(() -> out.addVolunteer(newVolunteer));
    }
    @Test
    public void getVolunteerExceptionCheckPhone() {
        Volunteer newVolunteer = generateVolunteer();
        newVolunteer.setPhoneNumber(" ");
        assertThatExceptionOfType(InvalidInputDataException.class)
                .isThrownBy(() -> out.getVolunteerByPhone(newVolunteer.getPhoneNumber()));
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