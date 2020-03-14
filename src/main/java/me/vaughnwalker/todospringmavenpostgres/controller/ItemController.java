package me.vaughnwalker.todospringmavenpostgres.controller;

import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemDTO;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemResponseDTO;
import me.vaughnwalker.todospringmavenpostgres.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ItemResponseDTO findItem(@PathVariable long itemId) {
        Item item = itemService.findById(itemId);
        return ItemResponseDTO.serializeFromItem(item);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDTO createItem(@Valid @RequestBody ItemDTO itemDTO) {
        Item item = new Item();
        item.setDescription(itemDTO.getDescription());
        Item savedItem = itemService.save(item);
        return ItemResponseDTO.serializeFromItem(savedItem);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<ItemResponseDTO> findAll() {
        List<Item> items = itemService.findAll();
        List<ItemResponseDTO> itemResponseDTOList = new ArrayList<>();
        for (Item item : items) {
            itemResponseDTOList.add(ItemResponseDTO.serializeFromItem(item));
        }
        return itemResponseDTOList;
    }
}
