package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataExceptionHandler {

    @ExceptionHandler(DateTimeOfDailyReportParseException.class)
    public ResponseEntity<?> dataBedRequest(BadParamException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
