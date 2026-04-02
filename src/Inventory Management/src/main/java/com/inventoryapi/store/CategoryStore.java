package com.inventoryapi.store;

import com.inventoryapi.db.DatabaseConnection;
import com.inventoryapi.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryStore {

    public Category save(Category category) {
        String sql = "INSERT INTO categories (id, name, description, created_at) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.setString(4, category.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save category: " + e.getMessage(), e);
        }
        return category;
    }

    public Optional<Category> findById(String id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapCategory(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find category: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Category> findAll() {
        String sql = "SELECT * FROM categories ORDER BY name ASC";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) categories.add(mapCategory(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find categories: " + e.getMessage(), e);
        }
        return categories;
    }

    public Category update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update category: " + e.getMessage(), e);
        }
        return category;
    }

    public void delete(String id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete category: " + e.getMessage(), e);
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM categories WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check category: " + e.getMessage(), e);
        }
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getString("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getString("created_at"));
        return category;
    }
}