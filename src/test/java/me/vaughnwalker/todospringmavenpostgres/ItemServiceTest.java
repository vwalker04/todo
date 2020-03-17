package me.vaughnwalker.todospringmavenpostgres;

import me.vaughnwalker.todospringmavenpostgres.exception.ArgumentNullException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
        Item itemToSave = new Item(1L, "a description");

        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(itemToSave);

        Item actualItem = itemService.save(itemToSave);

        assertThat(actualItem).isEqualTo(itemToSave);
    }

    @Test
    void save_throwsArgumentNullExceptionWhenEmpty() {
        Item empty = new Item();
        empty.setDescription("");

        assertThatExceptionOfType(ArgumentNullException.class).isThrownBy(() ->
                itemService.save(empty));
    }

    @Test
    void updateItem_returnsUpdatedItem() {
        Item oldItem = new Item(1L, "old description");
        Item updatedItem = new Item(oldItem.getId(), "new description");

        when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));
        when(itemRepository.save((Mockito.any(Item.class)))).thenReturn(updatedItem);

        Item actualItem = itemService.updateItem(updatedItem);

        assertThat(actualItem.getId()).isEqualTo(oldItem.getId());
        assertThat(actualItem.getDescription()).isEqualTo(updatedItem.getDescription());
    }

    @Test
    void updatedItem_throwsArgumentNullExceptionWhenEmpty() {
        Item invalidItem = new Item(1L, "");

        when(itemRepository.findById(invalidItem.getId())).thenReturn(Optional.of(invalidItem));

        assertThatExceptionOfType(ArgumentNullException.class).isThrownBy(() ->
                itemService.updateItem(invalidItem));
    }
}