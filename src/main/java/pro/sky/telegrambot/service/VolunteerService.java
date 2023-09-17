package pro.sky.telegrambot.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;

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

}
