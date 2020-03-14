package me.vaughnwalker.todospringmavenpostgres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "The item you were looking for was not found.";

    public ItemNotFoundException() {
        super(NOT_FOUND);
    }
}
