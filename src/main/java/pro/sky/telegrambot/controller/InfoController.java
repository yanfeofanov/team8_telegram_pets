package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.service.InfoService;

import java.util.Collection;
import java.util.List;

/**
 * класс содержит эндпойнты для управления информационными данными о приюте. Можно добавлять и обновлять информацию
 */
@RestController
@RequestMapping("/info")
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @PostMapping("/addInfo")
    public Info addInfo(@RequestBody Info newInfo) {
        return infoService.addInfo(newInfo);
    }

    @PutMapping("/update/{id}")
    public Info updateInfo(@PathVariable int id,@RequestBody Info info) {
        return infoService.updateInfoShelter(id,info);
    }

    /**
     * метод выводит всю информацию по выбранному приюту
     *
     * @param id идентификатор приюта
     * @return список с информацией о приюте
     */
    @GetMapping("/{id}")
    public Collection<Info> getAllInfoByShelter(@PathVariable int id) {
        return infoService.findByShelterIdInfo(id);
    }
}