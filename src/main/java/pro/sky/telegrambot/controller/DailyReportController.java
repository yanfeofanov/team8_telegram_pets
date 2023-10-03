package pro.sky.telegrambot.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.DailyReport;
import pro.sky.telegrambot.service.DailyReportService;
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
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный аргумент"
                    )
            }
    )
    @GetMapping("/{check}")
    public ResponseEntity<Collection<DailyReport>> getCheckedOrNotReport(@PathVariable Boolean check) {
        Collection<DailyReport> checkedReports = null;
        try {
            checkedReports = dailyReportService.getCheckedDailyReport(check);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        }
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
                            responseCode = "400",
                            description = "Неверный аргумент"
                    )
            }
    )
    @GetMapping("/owner/{id}")
    public ResponseEntity<Collection<DailyReport>> getAllDailyReportByOwnerId(@PathVariable Integer id) {
        Collection<DailyReport> ownerReports = null;
        try {
            ownerReports = dailyReportService.getAllDailyReportByPetOwner(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(ownerReports);
    }

    @Operation(
            summary = "Вывести отчет по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "отчетов выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DailyReport.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный аргумент"
                    )
            }
    )
    @GetMapping("/dailyReport/{id}")
    public ResponseEntity<DailyReport> getDailyReportById(@PathVariable Long id){
        DailyReport dailyReports = null;
        try {
            dailyReports = dailyReportService.getDailyReportById(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();

        }
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
                            responseCode = "400",
                            description = "Неверный аргумент"
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
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный аргумент"
                    )
            }
    )

    @GetMapping("/date")
    public ResponseEntity<Collection<DailyReport>> getAllDailyReportByDate(
            @RequestParam @Parameter(description = "Дата в формате YYYY-MM-DD") String date) {
        Collection<DailyReport> reports = dailyReportService.getAllDailyReportByDate(date);
        return ResponseEntity.ok(reports);
    }
}

