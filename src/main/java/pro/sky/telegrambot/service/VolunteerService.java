package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;


@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);
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
        List<Volunteer> volunteers = volunteerRepository.findAllByPhoneNumber(newVolunteer.getPhoneNumber());
        if (newVolunteer.getPhoneNumber() == null) {
            throw new InvalidInputDataException("Необходимо ввести корректный номер телефона");
        } else if (volunteerRepository.findAll().contains(newVolunteer)) {
            throw new ErrorCollisionException("Такой волонтер уже работает");
        } else if (!volunteers.isEmpty()) {
            throw new ErrorCollisionException("Волонтер с таким телефоном уже существует");
        } else {
            return volunteerRepository.save(newVolunteer);
        }
    }

    public Volunteer deleteVolunteer(int id) {
        try {
            Volunteer deleteVolunteer = volunteerRepository.findById(id)
                    .orElseThrow(() -> new InvalidInputDataException("Такой волонтер не найден, проверьте данные"));
            volunteerRepository.delete(deleteVolunteer);
            return deleteVolunteer;
        } catch (InvalidInputDataException e) {
            logger.error("Ошибка", e);
            throw e;
        }
    }

    public Volunteer getVolunteerByPhone(String phoneNumber) {
        Volunteer volunteer = volunteerRepository.findVolunteerByPhoneNumber(phoneNumber);
        if (!phoneNumber.isBlank() && phoneNumber.equals(volunteer.getPhoneNumber())) {
            return volunteer;
        } else {
            throw new InvalidInputDataException("Проверьте корректность входных данных");
        }
    }

    public Collection<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
