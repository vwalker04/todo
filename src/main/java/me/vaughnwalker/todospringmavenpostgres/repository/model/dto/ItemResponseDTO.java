package me.vaughnwalker.todospringmavenpostgres.repository.model.dto;

import lombok.Data;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;

@Data
public class ItemResponseDTO {
    private long id;
    private String description;

    private ItemResponseDTO(Item item) {
        this.id = item.getId();
        this.description = item.getDescription();
    }

    public static ItemResponseDTO serializeFromItem(Item updatedItem) {
        return new ItemResponseDTO(updatedItem);
    }
}
