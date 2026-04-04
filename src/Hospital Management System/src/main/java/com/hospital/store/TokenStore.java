package com.hospital.store;

import com.hospital.cache.CacheService;
import com.hospital.db.DatabaseConnection;
import com.hospital.exception.DatabaseException;
import com.hospital.model.BlacklistedToken;
import com.hospital.model.RefreshToken;

import java.sql.*;
import java.util.Optional;

public class TokenStore {


    public void saveRefreshToken(RefreshToken token) {
        String sql = """
                INSERT INTO refresh_tokens
                (token, user_id, expires_at, revoked, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUserId());
            stmt.setString(3, token.getExpiresAt());
            stmt.setBoolean(4, token.isRevoked());
            stmt.setString(5, token.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("saveRefreshToken", e);
        }
    }

    public Optional<RefreshToken> findRefreshToken(String token) {
        String sql = "SELECT * FROM refresh_tokens WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRefreshToken(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findRefreshToken", e);
        }
        return Optional.empty();
    }

    public void revokeRefreshToken(String token) {
        String sql = "UPDATE refresh_tokens SET revoked = TRUE WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("revokeRefreshToken", e);
        }
    }

    public void revokeAllUserTokens(String userId) {
        String sql = """
                UPDATE refresh_tokens
                SET revoked = TRUE WHERE user_id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("revokeAllUserTokens", e);
        }
    }

    public void blacklistToken(BlacklistedToken token) {
        java.time.Instant expiry = java.time.Instant.parse(token.getExpiresAt());
        java.time.Instant now    = java.time.Instant.now();
        long ttl = Math.max(1,
                expiry.getEpochSecond() - now.getEpochSecond());

        CacheService.set(
                CacheService.keyBlacklist(token.getTokenId()),
                "1",
                (int) ttl
        );

        // Also persist to DB for audit trail
        String sql = """
                INSERT INTO blacklisted_tokens
                (token_id, expires_at, blacklisted_at)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token.getTokenId());
            stmt.setString(2, token.getExpiresAt());
            stmt.setString(3, token.getBlacklistedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("blacklistToken", e);
        }
    }

    public boolean isBlacklisted(String jwtId) {
        return CacheService.exists(CacheService.keyBlacklist(jwtId));
    }


    private RefreshToken mapRefreshToken(ResultSet rs) throws SQLException {
        RefreshToken token = new RefreshToken();
        token.setToken(rs.getString("token"));
        token.setUserId(rs.getString("user_id"));
        token.setExpiresAt(rs.getString("expires_at"));
        token.setRevoked(rs.getBoolean("revoked"));
        token.setCreatedAt(rs.getString("created_at"));
        return token;
    }
}