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
    public boolean exists(String id) {
        String sql = "SELECT id FROM dish WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du plat " + id);
        }
    }

    public void updateAssociations(String dishId, List<Ingredient> ingredients) {
        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        String insertSql = "INSERT INTO dish_ingredient (id_dish, id_ingredient) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int idPlat = Integer.parseInt(dishId);

                try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
                    psDelete.setInt(1, idPlat);
                    psDelete.executeUpdate();
                }

                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    for (Ingredient ing : ingredients) {
                        psInsert.setInt(1, idPlat);
                        psInsert.setInt(2, Integer.parseInt(ing.getId()));
                        psInsert.executeUpdate();
                    }
                    psInsert.executeUpdate();
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Erreur lors de la mise à jour des ingrédients : " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion SQL");
        }
    }
}
