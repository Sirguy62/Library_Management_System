package organise.creditrisk.risk.repository;

import organise.creditrisk.infrastructure.db.BaseRepository;
import organise.creditrisk.risk.model.RiskScore;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RiskRepository extends BaseRepository {

    public void save(RiskScore score) {

        String sql =
                "INSERT INTO risk_scores (loan_id, score, level, reason) VALUES (?, ?, ?, ?)";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, score.getLoanId());
            ps.setInt(2, score.getScore());
            ps.setString(3, score.getLevel().name());
            ps.setString(4, score.getReason());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}