package pro.sky.telegrambot.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    Random random = new Random();

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Nullable
    public Volunteer getRandomVolunteer() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        return volunteers.get(random.nextInt(volunteers.size()));
    }

    public Volunteer addVolunteer(Volunteer newVolunteer) {
        if (!allFieldsNotNull(newVolunteer)) {
            throw new InvalidInputDataException("Необходимо ввести все данные");
        }
        if (volunteerRepository.findAll().contains(newVolunteer)) {
            throw new ErrorCollisionException("Такой волонтер уже работает");
        } else {
            return volunteerRepository.save(newVolunteer);
        }
    }

    public boolean allFieldsNotNull(Volunteer volunteer) {
        Field[] fields = Volunteer.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(volunteer);
                if (value == null) {
                    return false;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public Volunteer deleteVolunteer(int id) {
        Volunteer deleteVolunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ErrorCollisionException("Такой волонтер не найден, проверьте данные"));
        volunteerRepository.delete(deleteVolunteer);
        return deleteVolunteer;
    }

    public Volunteer geyVolunteerByPhone(String phoneNumber) {
        Volunteer volunteer = volunteerRepository.findVolunteerByPhoneNumber(phoneNumber);
        if (!phoneNumber.isEmpty() || phoneNumber.equals(volunteer.getPhoneNumber())) {
            return volunteer;
        } else {
            throw new InvalidInputDataException("Проверьте корректность входных данных");
        }
    }

    public Collection<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
