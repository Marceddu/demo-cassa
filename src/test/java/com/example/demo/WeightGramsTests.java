package com.example.demo;

import com.example.demo.model.Dish;
import com.example.demo.repo.DishRepository;
import com.example.demo.repo.OrderItemV2Repository;
import com.example.demo.util.WeightFormatUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeightGramsTests {
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepository;
    @Autowired OrderItemV2Repository orderItemV2Repository;

    @Test
    void savesWeightSnapshotFromDish() throws Exception {
        Dish d = dishRepository.findAllByOrderByNameAsc().stream().findFirst().orElseThrow();
        d.setWeightGrams(720);
        dishRepository.save(d);

        String body = """
                {"status":"NEW","items":[{"dishId":%d,"qty":1}]}
                """.formatted(d.getId());
        mockMvc.perform(post("/setordine").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

        assertThat(orderItemV2Repository.findAll())
                .anyMatch(i -> i.getDishId().equals(d.getId()) && Integer.valueOf(720).equals(i.getWeightGramsSnapshot()));
    }

    @Test
    void formatsItalianWeight() {
        assertThat(WeightFormatUtil.format(720)).isEqualTo("720 g");
        assertThat(WeightFormatUtil.format(1000)).isEqualTo("1,000 kg");
        assertThat(WeightFormatUtil.format(1120)).isEqualTo("1,120 kg");
    }
}
