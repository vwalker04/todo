package me.vaughnwalker.todospringmavenpostgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vaughnwalker.todospringmavenpostgres.controller.ItemController;
import me.vaughnwalker.todospringmavenpostgres.exception.ArgumentNullException;
import me.vaughnwalker.todospringmavenpostgres.exception.ItemNotFoundException;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemDTO;
import me.vaughnwalker.todospringmavenpostgres.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String ITEM_URL = "/item/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void findItemById_shouldReturnItemFromService() throws Exception {
        final long id = 123L;
        final String description = "a description";
        Item mockItem = new Item(id, description);

        when(itemService.findById(id)).thenReturn(mockItem);

        this.mockMvc.perform(get(ITEM_URL + id))
                .andDo((print()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    void findItemById_whenNotExists_shouldThrowItemNotFoundException() throws Exception {
        when(itemService.findById(Mockito.anyLong())).thenThrow(ItemNotFoundException.class);
        this.mockMvc.perform(get(ITEM_URL + "{itemId}", Mockito.anyLong())).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void createItem_returnsItem() throws Exception {
        Item itemToSave = new Item(1L, "a description");

        String body = mapper.writeValueAsString(itemToSave);

        when(itemService.save(Mockito.any())).thenReturn(itemToSave);

        this.mockMvc.perform(post(ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemToSave.getId()))
                .andExpect(jsonPath("$.description").value(itemToSave.getDescription()));
    }

    @Test
    void createItem_emptyString_shouldThrowArgumentNullException() throws Exception {
        ItemDTO emptyDescription = new ItemDTO();
        emptyDescription.setDescription("");

        Item itemToSave = new Item();
        itemToSave.setDescription(emptyDescription.getDescription());

        String body = mapper.writeValueAsString(emptyDescription);

        when(itemService.save(itemToSave)).thenThrow(ArgumentNullException.class);

        this.mockMvc.perform(post(ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_returnsEmptyList() throws Exception {
        List<Item> emptyList = Collections.emptyList();

        when(itemService.findAll()).thenReturn(emptyList);

        this.mockMvc.perform(get(ITEM_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findAll_returnsItemList() throws Exception {
        Item itemOne = new Item();
        String descriptionOne = "first description";
        itemOne.setDescription(descriptionOne);

        Item itemTwo = new Item();
        String descriptionTwo = "second description";
        itemTwo.setDescription(descriptionTwo);

        List<Item> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        when(itemService.findAll()).thenReturn(items);

        this.mockMvc.perform(get(ITEM_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].description").value(descriptionOne))
                .andExpect(jsonPath("$.[1].description").value(descriptionTwo));
    }
}