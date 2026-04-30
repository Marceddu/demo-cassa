package com.example.demo;

import com.example.demo.model.Dish;
import com.example.demo.model.Kitchen;
import com.example.demo.repo.DishRepository;
import com.example.demo.repo.KitchenRepository;
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
	public org.springframework.boot.CommandLineRunner seedDishes(DishRepository dishRepository, KitchenRepository kitchenRepository) {
		return args -> {
			if (dishRepository.count() > 0) return;
			Kitchen panini = kitchenRepository.findByNameIgnoreCase("Panini").orElseThrow();
			Kitchen cucinaDentro = kitchenRepository.findByNameIgnoreCase("Cucina Dentro").orElseThrow();
			create(dishRepository, "PRUPPEDDA", "8.00", panini);
			create(dishRepository, "PANE VRATTAU", "7.00", panini);
			create(dishRepository, "COMPLETO", "10.00", cucinaDentro);
			create(dishRepository, "PANE VRATTAU SENZA LATTOSIO", "7.50", panini);
			create(dishRepository, "COMPLETO SENZA LATTOSIO", "10.50", cucinaDentro);
		};
	}

	private void create(DishRepository repo, String name, String price, Kitchen kitchen) {
		Dish d = new Dish();
		d.setName(name);
		d.setPrice(new BigDecimal(price));
		d.setActive(true);
		d.setKitchen(kitchen);
		repo.save(d);
	}
}
