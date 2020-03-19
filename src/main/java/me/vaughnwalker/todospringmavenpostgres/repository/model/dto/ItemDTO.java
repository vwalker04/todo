package me.vaughnwalker.todospringmavenpostgres.repository.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String description;

    @JsonProperty("isDone")
    private boolean isDone = false;
}
