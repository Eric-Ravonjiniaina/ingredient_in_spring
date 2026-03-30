package com.hei_school.ingredient.controller;

import com.hei_school.ingredient.entity.Dish;
import com.hei_school.ingredient.repository.DishRepository;
import org.springframework.web.bind.annotation.*;

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
}
