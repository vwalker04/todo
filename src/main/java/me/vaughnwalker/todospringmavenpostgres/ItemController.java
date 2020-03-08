package me.vaughnwalker.todospringmavenpostgres;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping(path = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ItemResponseDTO findItem(@PathVariable long itemId) {
        Item item = itemRepository.findById(itemId).get();
        return ItemResponseDTO.serializeFromItem(item);
    }
}
