package com.hospital.store;

import com.hospital.db.DatabaseConnection;
import com.hospital.exception.DatabaseException;
import com.hospital.model.VerificationToken;

import java.sql.*;
import java.util.Optional;

public class VerificationStore {

    public void save(VerificationToken token) {
        String sql = """
                INSERT INTO verification_tokens
                (token, user_id, expires_at, used, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,  token.getToken());
            stmt.setString(2,  token.getUserId());
            stmt.setString(3,  token.getExpiresAt());
            stmt.setBoolean(4, token.isUsed());
            stmt.setString(5,  token.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("save verification token", e);
        }
    }

    public Optional<VerificationToken> findByToken(String token) {
        String sql = "SELECT * FROM verification_tokens WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapToken(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findVerificationToken", e);
        }
        return Optional.empty();
    }

    public void markUsed(String token) {
        String sql = """
                UPDATE verification_tokens
                SET used = TRUE WHERE token = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("markTokenUsed", e);
        }
    }

    private VerificationToken mapToken(ResultSet rs) throws SQLException {
        VerificationToken token = new VerificationToken();
        token.setToken(rs.getString("token"));
        token.setUserId(rs.getString("user_id"));
        token.setExpiresAt(rs.getString("expires_at"));
        token.setUsed(rs.getBoolean("used"));
        token.setCreatedAt(rs.getString("created_at"));
        return token;
    }
}