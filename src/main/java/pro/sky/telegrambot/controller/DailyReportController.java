package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.DailyReport;
import pro.sky.telegrambot.service.DailyReportService;

import java.util.ArrayList;
import java.util.Collection;
/**
 * класс содержит эндпойнты для управления ежедневными отчетами
 */
@RestController
@RequestMapping("/report")
public class DailyReportController {
    private final DailyReportService dailyReportService;

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }
    /**
     * метод может вывести проверенные, либо непроверенные отчеты
     * @param check проверен/непроверен
     * @return список очтетов
     */
    @GetMapping("/{check}")
    public Collection<DailyReport> getCheckedOrNotReport(@PathVariable Boolean check) {
        return new ArrayList<>();
    }
    /**
     * метод принимает качество отчета и в зависимости от его качества отправляет сообщение усыновителю
     * @param petOwnerId Id усыновителя
     * @param quality качество отчета
     * @return список очтетов
     */
    @PutMapping("/quality/{petOwnerId}")
    public void sendWarningByVolunteer(@PathVariable Long petOwnerId, @RequestParam boolean quality) {
    }
}
