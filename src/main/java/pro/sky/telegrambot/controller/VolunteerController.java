package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.service.VolunteerService;

import java.util.Collection;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
//    private final VolunteerService volunteerService;
//
//    public VolunteerController(VolunteerService volunteerService) {
//        this.volunteerService = volunteerService;
//    }
//    @PostMapping
//    public Volunteer addVolunteer(@RequestBody Volunteer newVolunteer) {
//        return;
//    }
//    @DeleteMapping("/{id}")
//    public Volunteer deleteVolunteer(@PathVariable int id) {
//        return;
//    }
//    @GetMapping("/{id}")
//    public Volunteer getVolunteer(@PathVariable int id) {
//        return;
//    }
//    @GetMapping("/all")
//    public Collection<Volunteer> getAllVolunteers() {
//        return;
//    }
//


}
