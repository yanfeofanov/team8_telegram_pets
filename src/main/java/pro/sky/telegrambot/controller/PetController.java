package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * класс содержит эндпойнты для управления базой данных питомцев, получением информацией об их усыновителях
 */
@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    @GetMapping("/{id}")
    public Pet findPet (@PathVariable int id) {
        checkParametersForNull(id);
        return petService.findPet(id);
    }
    @PostMapping
    public Pet addPet(@RequestBody Pet newPet) {
        checkParametersForNull(newPet);
        return petService.addPet(newPet);
    }
    @DeleteMapping("/{id}")
    public Pet deletePet (@PathVariable int id) {
        return petService.deletePet(id);
    }

    @GetMapping("/all")
    public Collection<Pet> getAllPets (){
        return new ArrayList<>();
    }
    /**
     * метод выводит всех собак или кошек
     * @param type тип животного (кошка или собака)
     * @return список кошек или собак
     */
    @GetMapping("/all/{type}")
    public Collection<Pet> getAllPetsByType(@PathVariable String type) {
        return new ArrayList<>();
    }
    /**
     * метод выводит всех питомцев по определнному усыновителю
     * @param id идентификатор усыновителя
     * @return список питомцев
     */
    @GetMapping("/owner/{id}")
    public Collection<Pet> getPetsByOwnerId(@PathVariable int id) {
        return new ArrayList<>();
    }
    /**
     * следующие два метода выводят всех кошек или собак, которых усыновили
     * @return список питомцев
     */
    @GetMapping("/ownerscats")
    public Collection<Pet> getOwnersCats() {
        return new ArrayList<>();
    }
    @GetMapping("/ownersdogs")
    public Collection<Pet> getOwnersDogs() {
        return new ArrayList<>();
    }

    private void checkParametersForNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                throw new BadParamException();
            }
        }
    }
}
