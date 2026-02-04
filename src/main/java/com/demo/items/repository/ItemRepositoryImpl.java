package com.demo.items.repository;

import com.demo.items.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository backed by ArrayList (as per requirement).
 *
 * Thread-safety:
 * - ArrayList is not thread-safe.
 * - Since a REST API can be hit concurrently, we synchronize around mutations & reads.
 *
 * Note: This data is ephemeral and will reset on application restart.
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final ArrayList<Item> store = new ArrayList<>();
    private final Object lock = new Object();
    private final AtomicLong seq = new AtomicLong(0);

    private static final Logger log = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    public Item save(Item item) {
        synchronized (lock) {
            // Always assign a new ID on save for "create"
            Item toPersist = Item.of(seq.incrementAndGet(), item.getName(), item.getDescription(), item.getPrice(), item.getCreatedAt());
            store.add(toPersist);
            log.debug("Item stored in-memory. id={}, size={}", toPersist.getId(), store.size());
            return toPersist;
        }
    }

    @Override
    public Optional<Item> findById(long id) {
        synchronized (lock) {
            return store.stream().filter(i -> i.getId() == id).findFirst();
        }
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        if (name == null) return false;
        String needle = name.trim().toLowerCase();
        synchronized (lock) {
            return store.stream().anyMatch(i -> i.getName().toLowerCase().equals(needle));
        }
    }
}
