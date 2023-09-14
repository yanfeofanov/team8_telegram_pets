package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

import java.util.Collection;

@RestController
@RequestMapping("/pets")
public class PetController {
//    private final PetService petService;
//
//    public PetController(PetService petService) {
//        this.petService = petService;
//    }
//    @GetMapping("/{id}")
//    public Pet findPet (@PathVariable int id) {
//        return;
//    }
//    @PostMapping
//    public Pet addPet(@RequestBody Pet newPet) {
//        return;
//    }
//    @DeleteMapping("/{id}")
//    public Pet deletePet (@PathVariable int id) {
//        return;
//    }
//
//    @GetMapping("/all")
//    public Collection<Pet> getAllPets (){
//        return;
//    }
//
//    @GetMapping("/all/{type}")
//    public Collection<Pet> getAllPetsByType(@PathVariable String type) {
//        return;
//    }
//    @GetMapping("/owner/{id}")
//    public Collection<Pet> getPetsByOwnerId(@PathVariable int id) {
//        return;
//    }
//    @GetMapping("/catowner")
//    public Collection<Pet> getCatsOwner() {
//        return;
//    }
//    @GetMapping("/dogowner")
//    public Collection<Pet> getCatsOwner() {
//        return;
//    }

}
