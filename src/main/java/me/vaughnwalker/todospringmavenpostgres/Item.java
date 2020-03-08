package me.vaughnwalker.todospringmavenpostgres;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull
    private long id;

    private String description;

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
