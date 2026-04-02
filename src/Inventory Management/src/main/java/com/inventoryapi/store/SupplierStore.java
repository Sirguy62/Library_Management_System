package com.inventoryapi.store;

import com.inventoryapi.db.DatabaseConnection;
import com.inventoryapi.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierStore {

    public Supplier save(Supplier supplier) {
        String sql = "INSERT INTO suppliers (id, name, email, phone, address, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getId());
            stmt.setString(2, supplier.getName());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getAddress());
            stmt.setString(6, supplier.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save supplier: " + e.getMessage(), e);
        }
        return supplier;
    }

    public Optional<Supplier> findById(String id) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapSupplier(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find supplier: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers ORDER BY name ASC";
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) suppliers.add(mapSupplier(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find suppliers: " + e.getMessage(), e);
        }
        return suppliers;
    }

    public Supplier update(Supplier supplier) {
        String sql = "UPDATE suppliers SET name=?, email=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getEmail());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getAddress());
            stmt.setString(5, supplier.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update supplier: " + e.getMessage(), e);
        }
        return supplier;
    }

    public void delete(String id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete supplier: " + e.getMessage(), e);
        }
    }

    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getString("id"));
        supplier.setName(rs.getString("name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setAddress(rs.getString("address"));
        supplier.setCreatedAt(rs.getString("created_at"));
        return supplier;
    }
}