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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EndpointSmokeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DishRepository dishRepository;

    @Test
    void getOrdiniRespondsOk() throws Exception {
        mockMvc.perform(get("/getordini"))
                .andExpect(status().isOk());
    }

    @Test
    void getTotaliRespondsOkWithTotalField() throws Exception {
        mockMvc.perform(get("/gettotali"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TOTAL").exists());
    }

    @Test
    void createAndDeleteOrderEndpointsRespondCorrectly() throws Exception {
        Dish dish = dishRepository.findAllByOrderByNameAsc().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No dishes available for smoke test"));

        String body = """
                {
                  "tableNo": "T1",
                  "notes": "smoke-order",
                  "status": "NEW",
                  "items": [
                    {
                      "dishId": %d,
                      "qty": 1,
                      "position": 0,
                      "itemNote": "test"
                    }
                  ]
                }
                """.formatted(dish.getId());

        MvcResult createResult = mockMvc.perform(post("/setordine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        String orderId = response.replaceAll(".*\"id\"\s*:\s*\"([^\"]+)\".*", "$1");
        assertThat(orderId).isNotBlank();

        mockMvc.perform(delete("/deleteordine/{id}", orderId))
                .andExpect(status().isNoContent());
    }

    @Test
    void setOrdineWithUnknownDishReturns404() throws Exception {
        String body = """
                {
                  "tableNo": "T404",
                  "notes": "missing-dish",
                  "status": "NEW",
                  "items": [
                    {
                      "dishId": 999999999,
                      "qty": 1,
                      "position": 0
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/setordine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void setOrdineWithInvalidQtyReturns400() throws Exception {
        Dish dish = dishRepository.findAllByOrderByNameAsc().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No dishes available for smoke test"));

        String body = """
                {
                  "tableNo": "T400",
                  "notes": "invalid-qty",
                  "status": "NEW",
                  "items": [
                    {
                      "dishId": %d,
                      "qty": 0,
                      "position": 0
                    }
                  ]
                }
                """.formatted(dish.getId());

        mockMvc.perform(post("/setordine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void setOrdineWithInvalidStatusReturns400() throws Exception {
        Dish dish = dishRepository.findAllByOrderByNameAsc().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No dishes available for smoke test"));

        String body = """
                {
                  "tableNo": "T400S",
                  "notes": "invalid-status",
                  "status": "INVALID",
                  "items": [
                    {
                      "dishId": %d,
                      "qty": 1,
                      "position": 0
                    }
                  ]
                }
                """.formatted(dish.getId());

        mockMvc.perform(post("/setordine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

}
