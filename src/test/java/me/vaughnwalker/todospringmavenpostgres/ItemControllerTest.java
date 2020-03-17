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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String ITEM_URL = "/items/";

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
        this.mockMvc.perform(get(ITEM_URL + "{id}", Mockito.anyLong()))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createItem_returnsItem() throws Exception {
        ItemDTO itemToSave = new ItemDTO();
        itemToSave.setDescription("hey I'm a thing.");

        String body = mapper.writeValueAsString(itemToSave);

        Item item = new Item(1L, itemToSave.getDescription());
        when(itemService.save(Mockito.any())).thenReturn(item);

        this.mockMvc.perform(post(ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(item.getId()))
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

    @Test
    void updateItem_returnsUpdatedItem() throws Exception {
        ItemDTO itemToUpdate = new ItemDTO();
        itemToUpdate.setDescription("new description of item.");

        Item item = new Item(123L, itemToUpdate.getDescription());

        String body = mapper.writeValueAsString(itemToUpdate);

        when(itemService.updateItem(Mockito.any(Item.class))).thenReturn(item);

        this.mockMvc.perform(put(ITEM_URL + item.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123L))
                .andExpect(jsonPath("$.description").value("new description of item."));
    }

    @Test
    void updateItem_whenNotExists_shouldThrowItemNotFoundException() throws Exception {
        ItemDTO nonExistingItem = new ItemDTO();
        nonExistingItem.setDescription("something");

        String body = mapper.writeValueAsString(nonExistingItem);

        when(itemService.updateItem(Mockito.any(Item.class))).thenThrow(ItemNotFoundException.class);

        this.mockMvc.perform(put(ITEM_URL + "{id}", Mockito.anyLong())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}