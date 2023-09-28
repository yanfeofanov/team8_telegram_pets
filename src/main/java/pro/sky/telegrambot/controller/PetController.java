package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

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
    public Pet findPet(@PathVariable int id) {
        checkParametersForNull(id);
        return petService.findPetById(id);
    }

    @PostMapping("/add")
    public Pet addPet(@RequestBody Pet newPet) {
        checkParametersForNull(newPet);
        return petService.addPet(newPet);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePet(@PathVariable int id) {
        petService.deletePet(id);
    }

    @GetMapping("/all")
    public Collection<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/all/leave")
    public Collection<Pet> getAllPetsThatHaveLeftShelter() {
        return petService.getAllPetsThatHaveLeftShelter();
    }

    @GetMapping("/all/available")
    public Collection<Pet> gatAllAvailable() {
        return petService.getAllAvailable();
    }

    @GetMapping("/all/on_probation")
    public Collection<Pet> gatAllOnProbation() {
        return petService.gatAllOnProbation();
    }

    /**
     * метод выводит всех собак или кошек
     *
     * @param type тип животного (кошка или собака)
     * @return список кошек или собак
     */
    @GetMapping("/all/{type}")
    public Collection<Pet> getAllPetsByType(@PathVariable String type) {
        checkParametersForNull(type);
        try {
            TypeOfPet typeOfPet = TypeOfPet.valueOf(type.toUpperCase());
            return petService.getAllPetsByType(typeOfPet);
        } catch (IllegalArgumentException e) {
            throw new BadParamException();
        }
    }

    @GetMapping("/all/{type}/available")
    public Collection<Pet> getAllAvailablePetsByType(@PathVariable String type) {
        checkParametersForNull(type);
        try {
            TypeOfPet typeOfPet = TypeOfPet.valueOf(type.toUpperCase());
            return petService.getAllAvailablePetsByType(typeOfPet);
        } catch (IllegalArgumentException e) {
            throw new BadParamException();
        }
    }

    /**
     * метод выводит всех питомцев по определнному усыновителю
     *
     * @param id идентификатор усыновителя
     * @return список питомцев
     */
    @GetMapping("/owner/{id}")
    public Collection<Pet> getPetsByOwnerId(@PathVariable int id) {
        checkParametersForNull(id);
        return petService.findPetsByPetOwnerId(id);
    }

    @GetMapping("/on_probation/owner/{id}")
    public Pet getPetOnProbationByOwnerId(@PathVariable int id) {
        checkParametersForNull(id);
        return petService.findPetOnProbationByPetOwnerId(id);
    }

    /**
     * метод выводит всех кошек или собак, которых усыновили
     *
     * @param type тип животного (кошка или собака)
     * @return список питомцев
     */
    @GetMapping("/all/{type}/on_probation")
    public Collection<Pet> getAllPetsOnProbationByType(@PathVariable String type) {
        checkParametersForNull(type);
        try {
            TypeOfPet typeOfPet = TypeOfPet.valueOf(type.toUpperCase());
            return petService.getAllPetsOnProbationByType(typeOfPet);
        } catch (IllegalArgumentException e) {
            throw new BadParamException();
        }
    }

    private void checkParametersForNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                throw new BadParamException();
            }
        }
    }
}
