package com.demo.items.service;

import com.demo.items.domain.Item;

public interface ItemService {
    Item create(Item item);
    Item getById(long id);
}
