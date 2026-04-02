package com.inventoryapi.store;

import com.inventoryapi.db.DatabaseConnection;
import com.inventoryapi.model.AuthToken;
import com.inventoryapi.model.User;

import java.sql.*;
import java.util.Optional;

public class UserStore {

    public User save(User user) {
        String sql = "INSERT INTO users (id, email, username, password_hash, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getEmail().trim().toLowerCase());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapUser(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapUser(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check email: " + e.getMessage(), e);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password_hash = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public void deleteByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    public void saveToken(AuthToken token) {
        String sql = "INSERT INTO auth_tokens (token, user_id, expires_at) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUserId());
            stmt.setString(3, token.getExpiresAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save token: " + e.getMessage(), e);
        }
    }

    public Optional<AuthToken> findToken(String rawToken) {
        String sql = "SELECT * FROM auth_tokens WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rawToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                AuthToken token = new AuthToken();
                token.setToken(rs.getString("token"));
                token.setUserId(rs.getString("user_id"));
                token.setExpiresAt(rs.getString("expires_at"));
                return Optional.of(token);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find token: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void deleteToken(String rawToken) {
        String sql = "DELETE FROM auth_tokens WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rawToken);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete token: " + e.getMessage(), e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setCreatedAt(rs.getString("created_at"));
        return user;
    }
}