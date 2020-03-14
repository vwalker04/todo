package me.vaughnwalker.todospringmavenpostgres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ArgumentNullException extends RuntimeException {

    private static final String ARGUMENT_NULL = "This field cannot be null or empty.";

    public ArgumentNullException() {
        super(ARGUMENT_NULL);
    }
}
