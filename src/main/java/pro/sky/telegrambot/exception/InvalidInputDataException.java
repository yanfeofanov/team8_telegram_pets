package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidInputDataException extends RuntimeException {
    public InvalidInputDataException(String message) {
        super(message);
    }
}
