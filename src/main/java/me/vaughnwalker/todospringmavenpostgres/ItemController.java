package me.vaughnwalker.todospringmavenpostgres;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    @GetMapping
    public @ResponseBody
    String findItem() {
        return "item";
    }
}
