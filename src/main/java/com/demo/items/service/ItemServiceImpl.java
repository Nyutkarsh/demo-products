package com.demo.items.service;

import com.demo.items.domain.Item;
import com.demo.items.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Instant;

/**
 * Implementation of ItemService.
 *
 * - Central place for business rules (e.g., uniqueness, transformations).
 * - Easy to swap repository implementation later (DB, cache, etc.).
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    public ItemServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Item create(Item item) {
        // Business rule example: unique name
        if (repository.existsByNameIgnoreCase(item.getName())) {
            log.warn("Rejecting create: duplicate item name. name='{}'", safe(item.getName()));
            throw new IllegalArgumentException("Item with same name already exists");
        }
        Item toCreate = Item.of(0, item.getName(), item.getDescription(), item.getPrice(), Instant.now());
        log.info("Item persisted. id={}, name='{}'", toCreate.getId(), safe(toCreate.getName()));
        return repository.save(toCreate);
    }

    @Override
    public Item getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.info("Item not found. id={}", id);
                    return new ItemNotFoundException(id);
                });
    }

    public static final class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(long id) {
            super("Item not found for id=" + id);
        }
    }

    /**
     * Prevents log injection / multi-line surprises in logs.
     */
    private static String safe(String input) {
        if (input == null) return null;
        return input.replace("\n", " ").replace("\r", " ").trim();
    }
}

