package com.hei_school.ingredient.entity;

import java.util.List;

public class Dish {
    private String id;
    private String name;
    private double unitPrice;
    private DishType dishType;
    private List<Ingredient> ingredients;
    private double margin;

    public Dish() {}
    public Dish(String id, String name, double unitPrice, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
    }

    public double getSellingPrice() {
        return this.unitPrice * (1 + this.margin);
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
