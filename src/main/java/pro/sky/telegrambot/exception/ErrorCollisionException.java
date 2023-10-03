package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ErrorCollisionException extends RuntimeException {
    public ErrorCollisionException(String message) {
        super(message);
    }
}
