package com.inventoryapi.store;

import com.inventoryapi.db.DatabaseConnection;
import com.inventoryapi.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductStore {

    public Product save(Product product) {
        String sql = "INSERT INTO products (id, name, description, category_id, supplier_id, " +
                "price, quantity, min_quantity, created_at) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCategoryId());
            stmt.setString(5, product.getSupplierId());
            stmt.setDouble(6, product.getPrice());
            stmt.setInt(7,    product.getQuantity());
            stmt.setInt(8,    product.getMinQuantity());
            stmt.setString(9, product.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save product: " + e.getMessage(), e);
        }
        return product;
    }

    public Optional<Product> findById(String id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapProduct(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find product: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY name ASC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) products.add(mapProduct(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find products: " + e.getMessage(), e);
        }
        return products;
    }

    public List<Product> findByCategoryId(String categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ? ORDER BY name ASC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) products.add(mapProduct(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find products: " + e.getMessage(), e);
        }
        return products;
    }

    public List<Product> findLowStock() {
        // quantity is above 0 but at or below the minimum threshold
        String sql = "SELECT * FROM products WHERE quantity > 0 " +
                "AND quantity <= min_quantity ORDER BY quantity ASC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) products.add(mapProduct(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find low stock: " + e.getMessage(), e);
        }
        return products;
    }

    public List<Product> findOutOfStock() {
        String sql = "SELECT * FROM products WHERE quantity = 0 ORDER BY name ASC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) products.add(mapProduct(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find out of stock: " + e.getMessage(), e);
        }
        return products;
    }

    public Product update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, category_id=?, supplier_id=?, " +
                "price=?, quantity=?, min_quantity=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCategoryId());
            stmt.setString(4, product.getSupplierId());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6,    product.getQuantity());
            stmt.setInt(7,    product.getMinQuantity());
            stmt.setString(8, product.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
        return product;
    }

    public void delete(String id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getString("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCategoryId(rs.getString("category_id"));
        product.setSupplierId(rs.getString("supplier_id"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setMinQuantity(rs.getInt("min_quantity"));
        product.setCreatedAt(rs.getString("created_at"));
        return product;
    }
}