package pro.sky.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadParamException extends RuntimeException{
    public BadParamException() {
    }

    public BadParamException(String message) {
        super(message);
    }
}
