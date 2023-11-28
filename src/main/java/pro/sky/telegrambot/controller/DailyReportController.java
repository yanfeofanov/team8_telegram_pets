package pro.sky.telegrambot.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.exception.DateTimeOfDailyReportParseException;
import pro.sky.telegrambot.model.DailyReport;
import pro.sky.telegrambot.model.PetOwner;
import pro.sky.telegrambot.service.DailyReportService;

import java.time.format.DateTimeParseException;
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

    @Operation(
            summary = "Вывести список проверенных (не проверенных) отчетов ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список отчетов выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DailyReport.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("/{check}")
    public ResponseEntity<Collection<DailyReport>> getCheckedOrNotReport(@PathVariable Boolean check) {
        Collection<DailyReport> checkedReports = dailyReportService.getCheckedDailyReport(check);
        return ResponseEntity.ok(checkedReports);
    }

    @Operation(
            summary = "Вывести список отчетов по айди владельца питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список отчетов выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DailyReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Владелец с таким id не найден"
                    )
            }
    )
    @GetMapping("/owner/{id}")
    public ResponseEntity<Collection<DailyReport>> getAllDailyReportByOwnerId(@PathVariable Integer id) {
        Collection<DailyReport> ownerReports = dailyReportService.getAllDailyReportByPetOwner(id);
        return ResponseEntity.ok(ownerReports);
    }

    @Operation(
            summary = "Вывести отчет по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчет выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DailyReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет с таким id не найден"
                    )
            }
    )
    @GetMapping("/dailyReport/{id}")
    public ResponseEntity<DailyReport> getDailyReportById(@PathVariable Long id){
        DailyReport dailyReports = dailyReportService.getDailyReportById(id);
        return ResponseEntity.ok(dailyReports);
    }

    @Operation(
            summary = "Изменяет статус отчета (проверен(true))",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "статус отчета изменен"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет с таким id не найден"
                    )
            }
    )

    @PutMapping("/idReport/{id}")
    public String checkedReport(@PathVariable Long id, @RequestParam boolean checked) {
        return dailyReportService.checkDailyReport(id,checked);
    }

    @Operation(
            summary = "Вывести список отчетов по дате",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список отчетов выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DailyReport.class)
                                    )
                            }
                    )
            }
    )

    @GetMapping("/date")
    public ResponseEntity<Collection<DailyReport>> getAllDailyReportByDate(
            @RequestParam @Parameter(description = "Дата в формате YYYY-MM-DD") String date) {
        Collection<DailyReport> reports = null;
        try {
            reports = dailyReportService.getAllDailyReportByDate(date);
        }catch (DateTimeParseException e) {
            throw new DateTimeOfDailyReportParseException();
        }

        return ResponseEntity.ok(reports);
    }
}

