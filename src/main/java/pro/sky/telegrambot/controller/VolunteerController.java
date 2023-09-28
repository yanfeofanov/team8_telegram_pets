package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.service.VolunteerService;

import java.util.ArrayList;
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
        return newVolunteer;
    }
    @DeleteMapping("/{id}")
    public Volunteer deleteVolunteer(@PathVariable int id) {
        return new Volunteer();
    }
    @GetMapping("/{id}")
    public Volunteer getVolunteer(@PathVariable int id) {
        return new Volunteer();
    }
    @GetMapping("/all")
    public Collection<Volunteer> getAllVolunteers() {
        return new ArrayList<>();
    }



}
