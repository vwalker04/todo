package me.vaughnwalker.todospringmavenpostgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vaughnwalker.todospringmavenpostgres.repository.model.dto.ItemDTO;
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
    private static final String ITEM_DESCRIPTION = "This is the first test item";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saveAndRetrieveItem_happyPath() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setDescription(ITEM_DESCRIPTION);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(itemDTO);

        this.mockMvc.perform(post(ITEM_URL)
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION));

        this.mockMvc.perform(get(ITEM_URL + "{itemId}", 1)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(ITEM_DESCRIPTION));
    }

}