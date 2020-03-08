package me.vaughnwalker.todospringmavenpostgres;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ItemResponseDTO findItem(@PathVariable long itemId) {
        //TODO: Handle this
        Item item = itemRepository.findById(itemId).get();
        return ItemResponseDTO.serializeFromItem(item);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDTO createItem(@RequestBody ItemDTO itemDTO) {
        Item item = new Item();
        item.setDescription(itemDTO.getDescription());
        Item savedItem = itemRepository.save(item);
        return ItemResponseDTO.serializeFromItem(savedItem);
    }
}
