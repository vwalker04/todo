package me.vaughnwalker.todospringmavenpostgres;

import me.vaughnwalker.todospringmavenpostgres.controller.ItemController;
import me.vaughnwalker.todospringmavenpostgres.exception.ItemNotFoundException;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import me.vaughnwalker.todospringmavenpostgres.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerIntegrationTest {

    private static final String ITEM_URL = "/item/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

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
    void findItemById_whenNotExists_shouldReturnItemNotFound() throws Exception {
        when(itemService.findById(Mockito.anyLong())).thenThrow(ItemNotFoundException.class);
        this.mockMvc.perform(get(ITEM_URL + "{itemId}", Mockito.anyLong())).andDo(print()).andExpect(status().is4xxClientError());
    }

//    @Test
//    void saveAndRetrieveItem_happyPath() throws Exception {
//        String description = "something here";
//        ItemDTO itemDTO = new ItemDTO();
//        itemDTO.setDescription(description);
//
//        String jsonPayload = mapper.writeValueAsString(itemDTO);
//
//        this.mockMvc.perform(post(ITEM_URL)
//                .content(jsonPayload)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.description").value(description));
//
//        this.mockMvc.perform(get(ITEM_URL + "{itemId}", 1))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.description").value(description));
//    }

//    @Test
//    void findAll_returnsItemList() throws Exception {
//        ItemResponseDTO itemOne = new ItemResponseDTO();
//        String descriptionOne = "first description";
//        itemOne.setDescription(descriptionOne);
//
//        ItemResponseDTO itemTwo = new ItemResponseDTO();
//        String descriptionTwo = "second description";
//        itemTwo.setDescription(descriptionTwo);
//
//        String jsonPayload = mapper.writeValueAsString(itemOne);
//        this.mockMvc.perform(post(ITEM_URL)
//                .content(jsonPayload)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        String jsonPayload2 = mapper.writeValueAsString(itemTwo);
//        this.mockMvc.perform(post(ITEM_URL)
//                .content(jsonPayload2)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        this.mockMvc.perform(get(ITEM_URL))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.[0].description").value(descriptionOne))
//                .andExpect(jsonPath("$.[1].description").value(descriptionTwo));
//    }
}