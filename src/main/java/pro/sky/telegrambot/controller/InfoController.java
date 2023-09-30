package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.service.InfoService;

import java.util.ArrayList;
import java.util.Collection;

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

    @PostMapping
    public Info addInfo(@RequestBody Info newInfo) {
        return newInfo;
    }
    @PutMapping
    public Info updateInfo(@RequestBody Info info) {
        return info;
    }
    /**
     * метод выводит всю информацию по выбранному приюту
     * @param id идентификатор приюта
     * @return список с информацией о приюте
     */
    @GetMapping("/{id}")
    public Collection<Info> getAllInfoByShelter(@PathVariable int id) {
        return new ArrayList<>();
    }
}