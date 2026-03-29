package organise.creditrisk.payment.repository;

import organise.creditrisk.infrastructure.db.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IdempotencyRepository extends BaseRepository {

    public boolean exists(String key) {

        String sql = "SELECT 1 FROM idempotency_keys WHERE key = ?";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }

    public void save(String key) {

        String sql = "INSERT INTO idempotency_keys (key) VALUES (?)";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}