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
        String sql = "SELECT * FROM ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Ingredient(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("unit_price"),
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

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("unit_price"),
                            rs.getString("category")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
