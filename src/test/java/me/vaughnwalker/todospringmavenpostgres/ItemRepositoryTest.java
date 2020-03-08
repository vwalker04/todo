package me.vaughnwalker.todospringmavenpostgres;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void injectComponentsAreNotNull() {
        assertThat(itemRepository).isNotNull();
    }

    @Test
    void canSaveAndRetrieveItem() {
        Item item = new Item();
        item.setDescription("test description");

        Item savedItem = itemRepository.save(item);

        Item itemToRetrieve = itemRepository.findById(savedItem.getId()).orElseGet(() ->
                fail("Expected to retrieve an item but did not."));

        assertThat(itemToRetrieve.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void exceptionThrownWhenUnableToFindById() {
        assertThrows(NoSuchElementException.class, itemRepository.findById(Mockito.anyLong())::get);
    }


}