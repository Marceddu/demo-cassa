package com.example.demo;

import com.example.demo.model.Dish;
import com.example.demo.repo.DishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderOptionsAndReceiptTests {
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepository;

    @Test
    void optionsSavedOnOrderItem() throws Exception {
        String option = mockMvc.perform(post("/api/v1/order-options").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"Senza Lattosio\",\"active\":true,\"sortOrder\":1}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String optionId = option.replaceAll(".*\"id\":(\\d+).*", "$1");
        Dish dish = dishRepository.findAllByOrderByNameAsc().stream().findFirst().orElseThrow();

        String body = """
                {"status":"NEW","items":[{"dishId":%d,"qty":1,"optionIds":[%s]}]}
                """.formatted(dish.getId(), optionId);
        mockMvc.perform(post("/setordine").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/order-options")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Senza Lattosio")));
    }

    @Test
    void receiptCenteredVariants() throws Exception {
        Dish dish = dishRepository.findAllByOrderByNameAsc().stream().findFirst().orElseThrow();
        String body = """
                {"status":"NEW","items":[{"dishId":%d,"qty":12}]}
                """.formatted(dish.getId());
        mockMvc.perform(post("/setordine").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());

        String list = mockMvc.perform(get("/api/v1/kitchens/1/orders")).andReturn().getResponse().getContentAsString();
        String kitchenOrderId = list.replaceAll(".*\"id\":(\\d+).*", "$1");

        MvcResult r = mockMvc.perform(get("/api/v1/kitchen-orders/{id}/receipt", kitchenOrderId))
                .andExpect(status().isOk()).andReturn();
        String txt = r.getResponse().getContentAsString();
        assertThat(txt).contains("Ordine");
        assertThat(txt).contains("N ");
        assertThat(txt).contains("********");
    }
}
