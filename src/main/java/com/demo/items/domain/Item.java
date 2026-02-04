package com.demo.items.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class Item {
    private final long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Instant createdAt;

    private Item(long id, String name, String description, BigDecimal price, Instant createdAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.price = Objects.requireNonNull(price);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Item of(long id, String name, String description, BigDecimal price, Instant createdAt) {
        return new Item(id, name.trim(), description == null ? null : description.trim(), price, createdAt);
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Instant getCreatedAt() { return createdAt; }
}
