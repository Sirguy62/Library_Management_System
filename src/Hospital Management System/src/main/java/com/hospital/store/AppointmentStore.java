package com.hospital.store;

import com.hospital.cache.CacheService;
import com.hospital.db.DatabaseConnection;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.DatabaseException;
import com.hospital.model.Appointment;
import com.hospital.util.JsonUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentStore {

    public Appointment save(Appointment appointment) {
        String sql = """
                INSERT INTO appointments
                (id, doctor_id, patient_id, scheduled_at,
                 duration_minutes, status, notes, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getId());
            stmt.setString(2, appointment.getDoctorId());
            stmt.setString(3, appointment.getPatientId());
            stmt.setString(4, appointment.getScheduledAt());
            stmt.setInt(5,    appointment.getDurationMinutes());
            stmt.setString(6, appointment.getStatus().name());
            stmt.setString(7, appointment.getNotes());
            stmt.setString(8, appointment.getCreatedAt());
            stmt.executeUpdate();

            CacheService.delete(
                    CacheService.keyAppointments(appointment.getDoctorId()));

        } catch (SQLException e) {
            throw DatabaseException.wrap("save appointment", e);
        }
        return appointment;
    }

    public Optional<Appointment> findById(String id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapAppointment(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findAppointmentById", e);
        }
        return Optional.empty();
    }

    public List<Appointment> findByDoctorId(String doctorId) {
        String cacheKey = CacheService.keyAppointments(doctorId);
        String cached   = CacheService.get(cacheKey);
        if (cached != null) {
            try {
                Appointment[] arr = JsonUtil.fromJson(cached, Appointment[].class);
                return java.util.Arrays.asList(arr);
            } catch (Exception ignored) {}
        }

        String sql = """
                SELECT * FROM appointments
                WHERE doctor_id = ?
                ORDER BY scheduled_at ASC
                """;
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) appointments.add(mapAppointment(rs));

            try {
                CacheService.set(cacheKey,
                        JsonUtil.toJson(appointments),
                        CacheService.TTL_APPOINTMENTS);
            } catch (Exception ignored) {}

        } catch (SQLException e) {
            throw DatabaseException.wrap("findByDoctorId", e);
        }
        return appointments;
    }

    public List<Appointment> findByPatientId(String patientId) {
        String sql = """
                SELECT * FROM appointments
                WHERE patient_id = ?
                ORDER BY scheduled_at DESC
                """;
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) appointments.add(mapAppointment(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findByPatientId", e);
        }
        return appointments;
    }

    public Appointment update(Appointment appointment) {
        String sql = """
                UPDATE appointments
                SET status = ?, notes = ?
                WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getStatus().name());
            stmt.setString(2, appointment.getNotes());
            stmt.setString(3, appointment.getId());
            stmt.executeUpdate();

            CacheService.delete(
                    CacheService.keyAppointments(appointment.getDoctorId()));

        } catch (SQLException e) {
            throw DatabaseException.wrap("updateAppointment", e);
        }
        return appointment;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setId(rs.getString("id"));
        a.setDoctorId(rs.getString("doctor_id"));
        a.setPatientId(rs.getString("patient_id"));
        a.setScheduledAt(rs.getString("scheduled_at"));
        a.setDurationMinutes(rs.getInt("duration_minutes"));
        a.setStatus(AppointmentStatus.fromString(rs.getString("status")));
        a.setNotes(rs.getString("notes"));
        a.setCreatedAt(rs.getString("created_at"));
        return a;
    }
}