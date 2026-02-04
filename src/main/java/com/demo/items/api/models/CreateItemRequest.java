package com.demo.items.api.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateItemRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be <= 100 chars")
        String name,

        @Size(max = 500, message = "description must be <= 500 chars")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "price must be > 0")
        BigDecimal price
) {}

