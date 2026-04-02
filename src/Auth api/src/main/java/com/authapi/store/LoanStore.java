package com.authapi.store;

import com.authapi.db.DatabaseConnection;
import com.authapi.model.Loan;
import com.authapi.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LoanStore {

//    private final Map<String, Loan> loans = new HashMap<>();
//    private final Map<String, List<Payment>> payments = new HashMap<>();

    public Loan save(Loan loan) {
        String sql = "INSERT INTO loans (id, user_id, borrower_name, amount, collateral, " +
                "amount_paid, status, due_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, loan.getId());
            prep.setString(2, loan.getUserId());
            prep.setString(3, loan.getBorrowerName());
            prep.setDouble(4, loan.getAmount());
            prep.setDouble(5, loan.getCollateral());
            prep.setDouble(6, loan.getAmountPaid());
            prep.setString(7, loan.getStatus());
            prep.setString(8, loan.getDueDate());
            prep.setString(9, loan.getCreatedAt());
            prep.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save loan" + e.getMessage(), e);
        }
        return loan;
    }

    public Optional<Loan> findById(String loanId) {
        String sql = "SELECT * FROM loans WhERE id = ?";
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, loanId);
            ResultSet res = prep.executeQuery();
            if (res.next()) return Optional.of(mapLoan(res));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find loan" + e.getMessage(), e);
        }
        return Optional.empty();
    }


    // Get all loans belonging to a specific user
    public List<Loan> findByUserId(String userId) {
        String sql = "SELECT * FROM loans WHERE user_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, userId);
            ResultSet res = prep.executeQuery();
            while (res.next()) loans.add(mapLoan(res));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find loan" + e.getMessage(), e);
        }
        return loans;
    }


    public List<Loan> findAll() {
        String sql = "SELECT * FROM loans";
        List<Loan> loans = new ArrayList<>();
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            ResultSet res = prep.executeQuery();
            while (res.next()) loans.add(mapLoan(res));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find loan" + e.getMessage(), e);
        }
        return loans;
    }


    public Loan update(Loan loan) {
        String sql = "UPDATE loans SET borrower_name=?, amount=?, collateral=?, " +
                "amount_paid=?, status=?, due_date=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, loan.getBorrowerName());
            prep.setDouble(2, loan.getAmount());
            prep.setDouble(3, loan.getCollateral());
            prep.setDouble(4, loan.getAmountPaid());
            prep.setString(5, loan.getStatus());
            prep.setString(6, loan.getDueDate());
            prep.setString(7, loan.getId());
            prep.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update loan: " + e.getMessage(), e);
        }
        return loan;
    }

    public void delete(String loanId) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, loanId);
            prep.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete loan: " + e.getMessage(), e);
        }
    }

    public boolean exists(String loanId) {
        String sql = "SELECT 1 FROM loans WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, loanId);
            ResultSet rs = prep.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check loan: " + e.getMessage(), e);
        }
    }

    public Payment savePayment(Payment payment) {
        String sql = "INSERT INTO payments (id, loan_id, user_id, amount, note, paid_at) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, payment.getId());
            prep.setString(2, payment.getLoanId());
            prep.setString(3, payment.getUserId());
            prep.setDouble(4, payment.getAmount());
            prep.setString(5, payment.getNote());
            prep.setString(6, payment.getPaidAt());
            prep.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save payment: " + e.getMessage(), e);
        }
        return payment;
    }

    public List<Payment> findPaymentsByLoanId(String loanId) {
        String sql = "SELECT * FROM payments WHERE loan_id = ? ORDER BY paid_at ASC";
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, loanId);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) payments.add(mapPayment(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payments: " + e.getMessage(), e);
        }
        return payments;
    }

    // Count how many payments were missed for a loan
    // A missed payment = a month passed with no payment recorded
    public int countMissedPayments(Loan loan, List<Payment> payments) {
        java.time.Instant createdAt = java.time.Instant.parse(loan.getCreatedAt());
        java.time.Instant now       = java.time.Instant.now();
        long monthsPassed = java.time.temporal.ChronoUnit.MONTHS.between(
                createdAt.atZone(java.time.ZoneOffset.UTC),
                now.atZone(java.time.ZoneOffset.UTC)
        );
        long missed = monthsPassed - payments.size();
        return (int) Math.max(0, missed);

    }


    // Calculate how many days a loan is past its due date
    public int calculateDaysPastDue(Loan loan) {
        java.time.Instant dueDate = java.time.Instant.parse(loan.getDueDate());
        java.time.Instant now     = java.time.Instant.now();
        if (now.isAfter(dueDate)) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(dueDate, now);
        }
        return 0;
    }


    private Loan mapLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getString("id"));
        loan.setUserId(rs.getString("user_id"));
        loan.setBorrowerName(rs.getString("borrower_name"));
        loan.setAmount(rs.getDouble("amount"));
        loan.setCollateral(rs.getDouble("collateral"));
        loan.setAmountPaid(rs.getDouble("amount_paid"));
        loan.setStatus(rs.getString("status"));
        loan.setDueDate(rs.getString("due_date"));
        loan.setCreatedAt(rs.getString("created_at"));
        return loan;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getString("id"));
        payment.setLoanId(rs.getString("loan_id"));
        payment.setUserId(rs.getString("user_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setNote(rs.getString("note"));
        payment.setPaidAt(rs.getString("paid_at"));
        return payment;
    }

}





















