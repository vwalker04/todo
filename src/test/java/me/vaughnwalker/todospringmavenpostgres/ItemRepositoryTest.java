package me.vaughnwalker.todospringmavenpostgres;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void injectComponentsAreNotNull() {
        assertThat(itemRepository).isNotNull();
    }


}