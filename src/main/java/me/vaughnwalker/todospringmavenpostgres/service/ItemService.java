package me.vaughnwalker.todospringmavenpostgres.service;

import me.vaughnwalker.todospringmavenpostgres.exception.ArgumentNullException;
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

    public Item findById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    public Item save(Item item) {
        validateDescription(item);
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        Item lookUpItem = findById(item.getId());
        validateDescription(item);
        lookUpItem.setDescription(item.getDescription());
        lookUpItem.setDone(item.isDone());
        return itemRepository.save(lookUpItem);
    }

    private void validateDescription(Item item) {
        if (item.getDescription().isEmpty()) {
            throw new ArgumentNullException();
        }
    }
}
