package com.inventoryapi.store;

import com.inventoryapi.db.DatabaseConnection;
import com.inventoryapi.model.StockMovement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockStore {

    public StockMovement save(StockMovement movement) {
        String sql = "INSERT INTO stock_movements (id, product_id, type, quantity, note, moved_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movement.getId());
            stmt.setString(2, movement.getProductId());
            stmt.setString(3, movement.getType());
            stmt.setInt(4,    movement.getQuantity());
            stmt.setString(5, movement.getNote());
            stmt.setString(6, movement.getMovedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save movement: " + e.getMessage(), e);
        }
        return movement;
    }

    public List<StockMovement> findByProductId(String productId) {
        String sql = "SELECT * FROM stock_movements WHERE product_id = ? ORDER BY moved_at ASC";
        List<StockMovement> movements = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) movements.add(mapMovement(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find movements: " + e.getMessage(), e);
        }
        return movements;
    }

    // Total units added to a product
    public int totalIn(String productId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM stock_movements " +
                "WHERE product_id = ? AND type = 'IN'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to sum stock in: " + e.getMessage(), e);
        }
        return 0;
    }

    // Total units removed from a product
    public int totalOut(String productId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM stock_movements " +
                "WHERE product_id = ? AND type = 'OUT'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to sum stock out: " + e.getMessage(), e);
        }
        return 0;
    }

    private StockMovement mapMovement(ResultSet rs) throws SQLException {
        StockMovement movement = new StockMovement();
        movement.setId(rs.getString("id"));
        movement.setProductId(rs.getString("product_id"));
        movement.setType(rs.getString("type"));
        movement.setQuantity(rs.getInt("quantity"));
        movement.setNote(rs.getString("note"));
        movement.setMovedAt(rs.getString("moved_at"));
        return movement;
    }
}