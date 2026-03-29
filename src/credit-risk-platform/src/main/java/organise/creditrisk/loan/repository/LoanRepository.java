package organise.creditrisk.loan.repository;

import organise.creditrisk.infrastructure.db.BaseRepository;
import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.common.enums.LoanStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository extends BaseRepository {

    public List<Loan> findAllActiveLoans() {

        String sql = "SELECT * FROM loans WHERE status = 'ACTIVE'";

        Connection conn = getConnection();

        List<Loan> loans = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                loans.add(new Loan(
                        rs.getLong("id"),
                        rs.getLong("customer_id"),
                        rs.getDouble("amount"),
                        rs.getDouble("collateral_value"),
                        LoanStatus.valueOf(rs.getString("status")),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("last_payment_date").toLocalDate()
                ));
            }

            return loans;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}