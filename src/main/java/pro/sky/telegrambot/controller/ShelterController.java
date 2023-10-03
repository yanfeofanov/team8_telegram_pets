package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.service.ShelterService;

@RestController
@RequestMapping("shelter")
public class ShelterController {
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping()
    public Shelter addShelter(@RequestBody Shelter newShelter) {
        return shelterService.addShelter(newShelter);
    }

    @DeleteMapping()
    public void deleteShelter(@RequestBody int id) {
        shelterService.deleteShelterById(id);
    }

    @PutMapping()
    public Shelter replaceShelter(@RequestBody Shelter shelter) {
        return shelterService.updateShelter(shelter);
    }
}
