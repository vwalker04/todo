package me.vaughnwalker.todospringmavenpostgres;

public class ItemNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "The item you were looking for was not found.";

    public ItemNotFoundException() {
        super(NOT_FOUND);
    }
}
