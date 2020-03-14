package me.vaughnwalker.todospringmavenpostgres.repository.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
