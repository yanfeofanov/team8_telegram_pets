package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.service.PetOwnerService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * класс позваляет вести контроль за усыновителями в течении всего периода
 * класс содержит базовые CRUD операции
 */
@RestController
@RequestMapping("/pet_owner")
public class PetsOwnerController {
    private final PetOwnerService petOwnerService;

    public PetsOwnerController(PetOwnerService petOwnerService) {
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/{phoneNumber}")
    public PetOwner findPetOwner(@PathVariable String phoneNumber) {
        return petOwnerService.findPetOwnerByPhone(phoneNumber);
    }

    @PostMapping
    public PetOwner addPetOwner(@RequestBody PetOwner newPetOwner) {
        return petOwnerService.addPetOwner(newPetOwner);
    }

    @DeleteMapping("/{id}")
    public PetOwner deletePetOwner(@PathVariable int id) {
        return petOwnerService.deletePetOwnerById(id);
    }
    /**
     * Метод позволяют получить усыновителей кошек
     * @return список
     */
    @GetMapping("/all/cat_owners")
    public Collection<PetOwner> getCatOwners() {
        return petOwnerService.getCatOwners();
    }
    @GetMapping("/all/dog_owners")
    public Collection<PetOwner> getDogOwners() {
        return petOwnerService.getDogOwners();
    }
    /**
     * метод выводит усыновителей, которые на испытательном сроке
     * @return список
     */
    @GetMapping("/probation")
    public Collection<PetOwner> getProbationPetOwners() {
        return petOwnerService.getPetOwnersOnProbation();
    }
    /**
     * метод может изменить дату испытательного срока и изменить статус
     * @return обновленные данные усыновителя
     */
    @GetMapping("/probation/data/{owner_id}")
    public PetOwner changeDataProbation(@PathVariable(value = "owner_id") String ownerId) {
        return new PetOwner();
    }


}
