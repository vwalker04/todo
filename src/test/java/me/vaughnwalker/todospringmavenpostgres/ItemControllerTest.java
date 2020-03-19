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
    private ItemService itemService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void findItemById_shouldReturnItemFromService() throws Exception {
        final long id = 123L;
        final String description = "a description";
        Item mockItem = new Item(id, description, false);

        when(itemService.findById(id)).thenReturn(mockItem);

        this.mockMvc.perform(get(ITEM_URL + id)
                .characterEncoding("utf-8"))
                .andDo((print()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.isDone").value(false));
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

        Item item = new Item(1L, itemToSave.getDescription(), false);
        when(itemService.save(Mockito.any())).thenReturn(item);

        this.mockMvc.perform(post(ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.description").value(itemToSave.getDescription()))
                .andExpect(jsonPath("$.isDone").value(false));
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
                .content(body)
                .characterEncoding("utf-8"))
                .andDo(print())
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
        String itemOneDescription = "first description";
        Item itemOne = new Item(1L, itemOneDescription, false);

        String descriptionTwo = "second description";
        Item itemTwo = new Item(2L, descriptionTwo, true);

        List<Item> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        when(itemService.findAll()).thenReturn(items);

        this.mockMvc.perform(get(ITEM_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].description").value(itemOneDescription))
                .andExpect(jsonPath("$.[0].isDone").value(false))
                .andExpect(jsonPath("$.[1].description").value(descriptionTwo))
                .andExpect(jsonPath("$.[1].isDone").value(true));
    }

    @Test
    void updateItem_returnsUpdatedItem() throws Exception {
        String description = "new description of item.";
        long id = 123L;

        ItemDTO itemToUpdate = new ItemDTO();
        itemToUpdate.setDescription(description);
        Item item = new Item(id, itemToUpdate.getDescription(), false);

        String body = mapper.writeValueAsString(itemToUpdate);

        when(itemService.updateItem(Mockito.any(Item.class))).thenReturn(item);

        this.mockMvc.perform(put(ITEM_URL + item.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    void updateItem_makeAsDone_returnsUpdatedItem() throws Exception {
        ItemDTO itemToMakeAsDone = new ItemDTO("To rule the world", true);
        Item itemToReturn = new Item(909L, itemToMakeAsDone.getDescription(), itemToMakeAsDone.isDone());

        String body = mapper.writeValueAsString(itemToMakeAsDone);

        when(itemService.updateItem(Mockito.any(Item.class))).thenReturn(itemToReturn);

        this.mockMvc.perform(put(ITEM_URL + itemToReturn.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemToReturn.getId()))
                .andExpect(jsonPath("$.description").value(itemToMakeAsDone.getDescription()))
                .andExpect(jsonPath("$.isDone").value(true));
    }

    @Test
    void updateItem_whenNotExists_shouldThrowItemNotFoundException() throws Exception {
        ItemDTO nonExistingItem = new ItemDTO("for an item that doesn't exist", false);

        String body = mapper.writeValueAsString(nonExistingItem);

        when(itemService.updateItem(Mockito.any(Item.class))).thenThrow(ItemNotFoundException.class);

        this.mockMvc.perform(put(ITEM_URL + "{id}", Mockito.anyLong())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}