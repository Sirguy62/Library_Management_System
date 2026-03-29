package organise.creditrisk.alert.repository;

import organise.creditrisk.alert.model.Alert;
import organise.creditrisk.infrastructure.db.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AlertRepository extends BaseRepository {

    public void save(Alert alert) {

        String sql =
                "INSERT INTO alerts (loan_id, level, message, created_at) VALUES (?, ?, ?, ?)";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, alert.getLoanId());
            ps.setString(2, alert.getLevel().name());
            ps.setString(3, alert.getMessage());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(alert.getCreatedAt()));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}