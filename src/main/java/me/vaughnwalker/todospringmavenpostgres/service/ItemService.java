package me.vaughnwalker.todospringmavenpostgres.service;

import me.vaughnwalker.todospringmavenpostgres.exception.ItemNotFoundException;
import me.vaughnwalker.todospringmavenpostgres.repository.ItemRepository;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findBy(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }
}