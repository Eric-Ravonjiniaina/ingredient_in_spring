package com.hei_school.ingredient.controller;

import com.hei_school.ingredient.entity.Dish;
import com.hei_school.ingredient.entity.Ingredient;
import com.hei_school.ingredient.repository.DishRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }
    @GetMapping
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }
    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable String id,
            @RequestBody List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity
                    .status(400)
                    .body("error requestBody.");
        }
        if (!dishRepository.exists(id)) {
            return ResponseEntity
                    .status(404)
                    .body("Dish.id=" + id + " is not found");
        }
        try {
            dishRepository.updateAssociations(id, ingredients);
            return ResponseEntity.ok()
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(500)
                    .body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> createDishes(@RequestBody List<Dish> dishesToCreate) {
        List<Dish> createdDishes = new ArrayList<>();

        try {
            for (Dish d : dishesToCreate) {
                if (dishRepository.existsByName(d.getName())) {
                    return ResponseEntity.status(400)
                            .body("Dish.name=" + d.getName() + " already exists");
                }
                Dish saved = dishRepository.save(d);
                createdDishes.add(saved);
            }

            return ResponseEntity.status(201).body(createdDishes);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
