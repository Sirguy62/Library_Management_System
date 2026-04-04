package com.hospital.store;

import com.hospital.db.DatabaseConnection;
import com.hospital.enums.AuditEventType;
import com.hospital.exception.DatabaseException;
import com.hospital.model.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditStore {

    public void log(AuditLog auditLog) {
        String sql = """
                INSERT INTO audit_logs
                (id, user_id, event_type, description,
                 ip_address, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, auditLog.getId());
            stmt.setString(2, auditLog.getUserId());
            stmt.setString(3, auditLog.getEventType().name());
            stmt.setString(4, auditLog.getDescription());
            stmt.setString(5, auditLog.getIpAddress());
            stmt.setString(6, auditLog.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to write audit log: "
                    + e.getMessage());
        }
    }

    public List<AuditLog> findByUserId(String userId) {
        String sql = """
                SELECT * FROM audit_logs
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) logs.add(mapLog(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findAuditByUserId", e);
        }
        return logs;
    }

    public List<AuditLog> findByEventType(AuditEventType type) {
        String sql = """
                SELECT * FROM audit_logs
                WHERE event_type = ?
                ORDER BY created_at DESC
                LIMIT 100
                """;
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) logs.add(mapLog(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findAuditByType", e);
        }
        return logs;
    }

    private AuditLog mapLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getString("id"));
        log.setUserId(rs.getString("user_id"));
        log.setEventType(AuditEventType.fromString(rs.getString("event_type")));
        log.setDescription(rs.getString("description"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setCreatedAt(rs.getString("created_at"));
        return log;
    }
}