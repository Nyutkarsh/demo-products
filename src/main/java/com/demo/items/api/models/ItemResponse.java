package com.demo.items.api.models;

import java.math.BigDecimal;
import java.time.Instant;

public record ItemResponse(
        long id,
        String name,
        String description,
        BigDecimal price,
        Instant createdAt
) {}