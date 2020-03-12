package me.vaughnwalker.todospringmavenpostgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vaughnwalker.todospringmavenpostgres.repository.ItemRepository;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemDTO;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIntegrationTest {

    private static final String ITEM_URL = "/item/";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Autowired
    ItemRepository itemRepository;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void saveAndRetrieveItem_happyPath() throws Exception {
        String description = "something here";
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setDescription(description);

        String jsonPayload = mapper.writeValueAsString(itemDTO);

        this.mockMvc.perform(post(ITEM_URL)
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.description").value(description));

        this.mockMvc.perform(get(ITEM_URL + "{itemId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    void findAll_returnsItemList() throws Exception {
        ItemResponseDTO itemOne = new ItemResponseDTO();
        String descriptionOne = "first description";
        itemOne.setDescription(descriptionOne);

        ItemResponseDTO itemTwo = new ItemResponseDTO();
        String descriptionTwo = "second description";
        itemTwo.setDescription(descriptionTwo);

        String jsonPayload = mapper.writeValueAsString(itemOne);
        this.mockMvc.perform(post(ITEM_URL)
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON));

        String jsonPayload2 = mapper.writeValueAsString(itemTwo);
        this.mockMvc.perform(post(ITEM_URL)
                .content(jsonPayload2)
                .contentType(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(get(ITEM_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].description").value(descriptionOne))
                .andExpect(jsonPath("$.[1].description").value(descriptionTwo));
    }
}