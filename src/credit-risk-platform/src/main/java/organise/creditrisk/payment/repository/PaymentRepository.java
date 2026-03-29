package organise.creditrisk.payment.repository;

import organise.creditrisk.infrastructure.db.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaymentRepository extends BaseRepository {

    public void savePayment(Long loanId, double amount) {

        String sql = "INSERT INTO payments (loan_id, amount) VALUES (?, ?)";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, loanId);
            ps.setDouble(2, amount);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}