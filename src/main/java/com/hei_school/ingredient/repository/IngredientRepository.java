package com.hei_school.ingredient.repository;

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
public class IngredientRepository {
    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Ingredient(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Ingredient findById(String id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            String.valueOf(rs.getInt("id")),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("category")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public double getStockValue(String id, String at) {
        String sql = """
        SELECT 
            (SELECT COALESCE(initial_stock, 0) FROM ingredient WHERE id = ?) +
            COALESCE(SUM(CASE WHEN type = 'IN' THEN quantity ELSE -quantity END), 0) as current_stock
        FROM stock_movement 
        WHERE id_ingredient = ? AND creation_datetime <= CAST(? AS TIMESTAMP)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int intId = Integer.parseInt(id);
            ps.setInt(1, intId);
            ps.setInt(2, intId);
            ps.setString(3, at);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("current_stock");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL Stock: " + e.getMessage());
        }
        return 0.0;
    }
}
