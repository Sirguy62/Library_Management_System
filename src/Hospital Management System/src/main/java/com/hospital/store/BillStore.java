package com.hospital.store;

import com.hospital.db.DatabaseConnection;
import com.hospital.enums.BillStatus;
import com.hospital.exception.DatabaseException;
import com.hospital.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillStore {

    public Bill save(Bill bill) {
        String sql = """
                INSERT INTO bills
                (id, appointment_id, patient_id, amount,
                 discount, total, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bill.getId());
            stmt.setString(2, bill.getAppointmentId());
            stmt.setString(3, bill.getPatientId());
            stmt.setDouble(4, bill.getAmount());
            stmt.setDouble(5, bill.getDiscount());
            stmt.setDouble(6, bill.getTotal());
            stmt.setString(7, bill.getStatus().name());
            stmt.setString(8, bill.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("save bill", e);
        }
        return bill;
    }

    public Optional<Bill> findById(String id) {
        String sql = "SELECT * FROM bills WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapBill(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findBillById", e);
        }
        return Optional.empty();
    }

    public List<Bill> findByPatientId(String patientId) {
        String sql = """
                SELECT * FROM bills
                WHERE patient_id = ?
                ORDER BY created_at DESC
                """;
        List<Bill> bills = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) bills.add(mapBill(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findBillsByPatient", e);
        }
        return bills;
    }

    public void markPaid(String billId) {
        String sql = "UPDATE bills SET status = 'PAID' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, billId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("markBillPaid", e);
        }
    }

    private Bill mapBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setId(rs.getString("id"));
        bill.setAppointmentId(rs.getString("appointment_id"));
        bill.setPatientId(rs.getString("patient_id"));
        bill.setAmount(rs.getDouble("amount"));
        bill.setDiscount(rs.getDouble("discount"));
        bill.setTotal(rs.getDouble("total"));
        bill.setStatus(BillStatus.fromString(rs.getString("status")));
        bill.setCreatedAt(rs.getString("created_at"));
        return bill;
    }
}
