package com.demo.items.api;

import com.demo.items.api.models.CreateItemRequest;
import com.demo.items.api.models.ItemResponse;
import com.demo.items.domain.Item;
import com.demo.items.service.ItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Item resource.
 *
 * API design notes:
 * - Input uses DTOs to avoid coupling API contract with domain model.
 * - Output uses DTOs to keep response stable if domain changes later.
 */
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService service;

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    public ItemController(ItemService service) {
        this.service = service;
    }

    /**
     * Creates a new item.
     *
     * REST best practice: returns 201 Created + Location header pointing to the created resource.
     */
    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody CreateItemRequest req) {
        log.info("Create item request received. name='{}'", safe(req.name()));
        Item created = service.create(Item.of(
                0,
                req.name(),
                req.description(),
                req.price(),
                java.time.Instant.now()
        ));
        log.info("Item created successfully. id={}, name='{}'", created.getId(), safe(created.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    /**
     * Fetches a single item by its id.
     */
    @GetMapping("/{id}")
    public ItemResponse getById(@PathVariable("id") long id) {
        return toResponse(service.getById(id));
    }

    private static ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCreatedAt()
        );
    }

    /**
     * Prevents log injection / multi-line surprises in logs.
     */
    private static String safe(String input) {
        if (input == null) return null;
        return input.replace("\n", " ").replace("\r", " ").trim();
    }
}

