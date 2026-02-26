package com.example.demo;

import com.example.demo.model.Dish;
import com.example.demo.repo.DishRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public org.springframework.boot.CommandLineRunner seedDishes(DishRepository dishRepository) {
		return args -> {
			if (dishRepository.count() > 0) return;
			create(dishRepository, "PRUPPEDDA", "8.00");
			create(dishRepository, "PANE VRATTAU", "7.00");
			create(dishRepository, "COMPLETO", "10.00");
			create(dishRepository, "PANE VRATTAU SENZA LATTOSIO", "7.50");
			create(dishRepository, "COMPLETO SENZA LATTOSIO", "10.50");
		};
	}

	private void create(DishRepository repo, String name, String price) {
		Dish d = new Dish();
		d.setName(name);
		d.setPrice(new BigDecimal(price));
		d.setActive(true);
		repo.save(d);
	}
}
