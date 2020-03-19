package me.vaughnwalker.todospringmavenpostgres.controller;

import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemDTO;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemResponseDTO;
import me.vaughnwalker.todospringmavenpostgres.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ItemResponseDTO findItem(@PathVariable long id) {
        Item item = itemService.findById(id);
        return ItemResponseDTO.serializeFromItem(item);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDTO createItem(@RequestBody ItemDTO itemDTO) {
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

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDTO updateItem(@PathVariable long id, @RequestBody ItemDTO itemDTO) {
        Item itemToUpdate = new Item(id, itemDTO.getDescription(), itemDTO.isDone());
        Item updatedItem = itemService.updateItem(itemToUpdate);
        return ItemResponseDTO.serializeFromItem(updatedItem);
    }
}
