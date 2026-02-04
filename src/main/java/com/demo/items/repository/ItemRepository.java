package com.demo.items.repository;

import com.demo.items.domain.Item;

import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);
    Optional<Item> findById(long id);
    boolean existsByNameIgnoreCase(String name);
}
