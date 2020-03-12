package me.vaughnwalker.todospringmavenpostgres.repository;

import me.vaughnwalker.todospringmavenpostgres.repository.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
