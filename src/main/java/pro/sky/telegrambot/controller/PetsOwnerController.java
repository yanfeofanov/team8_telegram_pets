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

    @GetMapping("/phone")
    public PetOwner findPetOwner(@RequestParam(value = "phone") String phoneNumber) {
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
     * метод может изменить дату испытательного срока и изменить статус и отправить соотвествующие сообщение усыновителю
     * @param ownerId принимает id усыновителя
     * @param status принимает показатель нового статуса усыновителя:
     * 0 - означает, что испытельный срок пройден
     * 1 - испытательный срок продлен на 14 дней
     * 2 - испытательный срок продлен на 30 дней
     * 3 - испытательный срок не пройден
     * @return обновленные данные усыновителя
     */
    @PatchMapping("/probation/status")
    public PetOwner changeStatusProbation(@RequestParam int ownerId,
                                        @RequestParam int status) {
        return petOwnerService.changeProbationStatus(ownerId, status);
    }
}
