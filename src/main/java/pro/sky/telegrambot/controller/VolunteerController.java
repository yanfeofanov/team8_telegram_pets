package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.service.VolunteerService;

import java.util.Collection;

/**
 * класс управляет базой волонтеров, можно удалить, добавить, получить список всех волонтеров
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public Volunteer addVolunteer(@RequestBody Volunteer newVolunteer) {
        return volunteerService.addVolunteer(newVolunteer);
    }
    @DeleteMapping("/{id}")
    public Volunteer deleteVolunteer(@PathVariable int id) {
        return volunteerService.deleteVolunteer(id);
    }
    @GetMapping("/phone")
    public Volunteer getVolunteer(@RequestParam (value = "phone") String phoneNumber) {
        return volunteerService.getVolunteerByPhone(phoneNumber);
    }
    @GetMapping("/all")
    public Collection<Volunteer> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }



}
