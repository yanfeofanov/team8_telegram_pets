package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.service.CommunicationRequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.TimeZone;

@RestController
@RequestMapping("communication_request")
public class CommunicationRequestController {
    private final CommunicationRequestService communicationRequestService;

    public CommunicationRequestController(CommunicationRequestService communicationRequestService) {
        this.communicationRequestService = communicationRequestService;
    }

    @GetMapping(value = "all_for_period")
    public Collection<CommunicationRequest> getAllForPeriodByDone(@RequestParam(name = "startPeriod") String startPeriodStr,
                                                            @RequestParam(name = "endPeriod", required = false) String endPeriodStr,
                                                            @RequestParam boolean done) {
        checkParametersForNull(startPeriodStr, done);
        LocalDateTime startPeriod, endPeriod;
        try {
            startPeriod = LocalDateTime.parse(startPeriodStr);
        } catch (DateTimeParseException e) {
            throw new BadParamException();
        }
        if (endPeriodStr == null) {
            endPeriod = LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId());
        } else {
            try {
                endPeriod = LocalDateTime.parse(endPeriodStr);
            } catch (DateTimeParseException e) {
                throw new BadParamException();
            }
        }
        return communicationRequestService.getAllRequestForPeriodByDone(startPeriod, endPeriod, done);
    }

    @PostMapping("/{id}/check")
    public CommunicationRequest checkCommunicationRequest(@PathVariable int id, @RequestBody boolean done) {
        return communicationRequestService.checkCommunicationRequest(id, done);
    }
    private void checkParametersForNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                throw new BadParamException();
            }
        }
    }
}
