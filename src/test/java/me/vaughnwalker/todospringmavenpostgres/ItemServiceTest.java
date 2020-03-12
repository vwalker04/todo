package me.vaughnwalker.todospringmavenpostgres;

import me.vaughnwalker.todospringmavenpostgres.exception.ItemNotFoundException;
import me.vaughnwalker.todospringmavenpostgres.repository.ItemRepository;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import me.vaughnwalker.todospringmavenpostgres.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void findAllItems_noItemsExists_returnEmptyList() {
        List<Item> items = new ArrayList<>();

        when(itemRepository.findAll()).thenReturn(items);

        assertThat(itemService.findAll()).isEmpty();
    }

    @Test
    void findAllItems_returnsItems() {
        final String description1 = "description1";
        final String description2 = "description2";

        Item itemOne = new Item(1L, description1);
        Item itemTwo = new Item(2L, description2);

        List<Item> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        when(itemRepository.findAll()).thenReturn(items);

        List<Item> actualItems = itemService.findAll();

        assertThat(actualItems).hasSize(2);
        assertThat(actualItems.get(0).getDescription()).isEqualTo(description1);
        assertThat(actualItems.get(1).getDescription()).isEqualTo(description2);
    }

    @Test
    void findById_returnsItem() {
        long itemId = 1L;
        Item item = new Item(itemId, "some item");

        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        Item actualItem = itemService.findById(itemId);

        assertThat(actualItem.getId()).isEqualTo(itemId);
    }

    @Test
    void findById_throwsExceptionWhenNotFound() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ItemNotFoundException.class).isThrownBy(() ->
            itemService.findById(1234L));
    }

    @Test
    void save_returnsItem() {
        Item item = new Item(1L, "a description");

        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

        Item actualItem = itemRepository.save(item);

        assertThat(actualItem.getDescription()).isEqualTo(item.getDescription());
    }
}