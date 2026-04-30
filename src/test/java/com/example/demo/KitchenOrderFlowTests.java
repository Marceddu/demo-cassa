package com.example.demo;

import com.example.demo.repo.DishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KitchenOrderFlowTests {
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepository;

    @Test
    void oneKitchenOrderCreated() throws Exception {
        Long dishId = dishRepository.findAllByOrderByNameAsc().stream().filter(d -> d.getKitchen()!=null && d.getKitchen().getName().equals("Panini")).findFirst().orElseThrow().getId();
        String body = """
                {"status":"NEW","items":[{"dishId":%d,"qty":1}]}
                """.formatted(dishId);
        mockMvc.perform(post("/setordine").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/kitchens/1/orders")).andExpect(status().isOk()).andExpect(jsonPath("$[0].kitchenId").value(1));
    }

    @Test
    void twoKitchenOrdersAndCancelSingle() throws Exception {
        Long panini = dishRepository.findAllByOrderByNameAsc().stream().filter(d -> d.getKitchen()!=null && d.getKitchen().getName().equals("Panini")).findFirst().orElseThrow().getId();
        Long inside = dishRepository.findAllByOrderByNameAsc().stream().filter(d -> d.getKitchen()!=null && d.getKitchen().getName().equals("Cucina Dentro")).findFirst().orElseThrow().getId();
        String body = """
                {"status":"NEW","items":[{"dishId":%d,"qty":1},{"dishId":%d,"qty":1}]}
                """.formatted(panini, inside);
        mockMvc.perform(post("/setordine").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
        String list = mockMvc.perform(get("/api/v1/kitchens/1/orders")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String id = list.replaceAll(".*\"id\":(\\d+).*", "$1");
        mockMvc.perform(delete("/api/v1/kitchen-orders/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/kitchens/2/orders")).andExpect(status().isOk());
    }
}
