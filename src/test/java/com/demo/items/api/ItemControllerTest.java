package com.demo.items.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void create_and_get_item() throws Exception {
        String body = om.writeValueAsString(Map.of(
                "name", "Netflix Basic Plan",
                "description", "Streaming subscription",
                "price", new BigDecimal("199.00")
        ));

        String resp = mvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        long id = ((Number) om.readValue(resp, Map.class).get("id")).longValue();

        mvc.perform(get("/api/v1/items/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Netflix Basic Plan"));
    }

    @Test
    void validation_error_when_name_missing() throws Exception {
        String body = om.writeValueAsString(Map.of(
                "description", "Missing name",
                "price", new BigDecimal("10.00")
        ));

        mvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}

