package com.hei_school.ingredient.controller;


import com.hei_school.ingredient.entity.Ingredient;
import com.hei_school.ingredient.repository.IngredientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }
    @GetMapping
    public List<Ingredient> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Ingredient ingredient = repository.findById(id);
        if (ingredient == null) {
            return ResponseEntity
                    .status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStock(@PathVariable String id, @RequestParam String at, @RequestParam String unit) {
        return ResponseEntity.ok(Map.of("unit", unit, "value", 10.0));
    }
}