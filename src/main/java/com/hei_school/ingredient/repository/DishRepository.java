package com.hei_school.ingredient.repository;

import com.hei_school.ingredient.entity.Dish;
import com.hei_school.ingredient.entity.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<Dish> findAll() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id, name, selling_price FROM dish";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String name = rs.getString("name");
                double price = rs.getDouble("selling_price"); // Correction ici
                List<Ingredient> ingredients = findIngredientsByDishId(id);
                dishes.add(new Dish(id, name, price, ingredients));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    }
    private List<Ingredient> findIngredientsByDishId(String dishId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = """
        SELECT i.id, i.name, i.price, i.category 
        FROM ingredient i
        JOIN dish_ingredient di ON i.id = di.id_ingredient
        WHERE di.id_dish = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(dishId));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(new Ingredient(
                            String.valueOf(rs.getInt("id")),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("category")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des ingrédients du plat " + dishId);
        }
        return ingredients;
    }
}
