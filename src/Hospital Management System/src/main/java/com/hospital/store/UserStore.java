package com.hospital.store;

import com.hospital.cache.CacheService;
import com.hospital.db.DatabaseConnection;
import com.hospital.enums.UserRole;
import com.hospital.enums.UserStatus;
import com.hospital.exception.DatabaseException;
import com.hospital.model.Doctor;
import com.hospital.model.Nurse;
import com.hospital.model.Patient;
import com.hospital.model.base.Person;
import com.hospital.util.JsonUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserStore {


    public Person save(Person person) {
        String userSql = """
                INSERT INTO users
                (id, email, username, password_hash, role, status,
                 failed_attempts, locked_until, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(userSql)) {

            stmt.setString(1, person.getId());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getUsername());
            stmt.setString(4, person.getPasswordHash());
            stmt.setString(5, person.getRole().name());
            stmt.setString(6, person.getStatus().name());
            stmt.setInt(7,    person.getFailedAttempts());
            stmt.setString(8, person.getLockedUntil());
            stmt.setString(9, person.getCreatedAt());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw DatabaseException.wrap("save user", e);
        }

        saveRoleSpecificData(person);
        return person;
    }

    private void saveRoleSpecificData(Person person) {
        switch (person.getRole()) {

            case DOCTOR, ADMIN -> {
                Doctor doctor = (Doctor) person;
                String sql = """
                    INSERT INTO doctors
                    (id, specialization, license_number,
                     phone, available_from, available_to)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, doctor.getId());
                    stmt.setString(2, doctor.getSpecialization().name());
                    stmt.setString(3, doctor.getLicenseNumber());
                    stmt.setString(4, doctor.getPhone() != null
                            ? doctor.getPhone() : "");
                    stmt.setString(5, doctor.getAvailableFrom());
                    stmt.setString(6, doctor.getAvailableTo());
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    throw DatabaseException.wrap(
                            "save doctor data", e);
                }
            }

            case PATIENT -> {
                Patient patient = (Patient) person;
                String sql = """
            INSERT INTO patients
            (id, blood_group, phone, address, date_of_birth)
            VALUES (?, ?, ?, ?, ?)
            """;
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, patient.getId());
                    if (patient.getBloodGroup() != null) {
                        stmt.setString(2, patient.getBloodGroup().name());
                    } else {
                        stmt.setNull(2, java.sql.Types.VARCHAR);
                    }
                    stmt.setString(3, patient.getPhone() != null
                            ? patient.getPhone() : "");
                    stmt.setString(4, patient.getAddress() != null
                            ? patient.getAddress() : "");
                    stmt.setString(5, patient.getDateOfBirth() != null
                            ? patient.getDateOfBirth() : "");

                    System.out.println("DEBUG patient insert:");
                    System.out.println("  id="           + patient.getId());
                    System.out.println("  blood_group="  + (patient.getBloodGroup() != null ? patient.getBloodGroup().name() : "NULL"));
                    System.out.println("  phone="        + patient.getPhone());
                    System.out.println("  address="      + patient.getAddress());
                    System.out.println("  dateOfBirth="  + patient.getDateOfBirth());

                    stmt.executeUpdate();

                } catch (SQLException e) {
                    System.err.println("SQL ERROR: " + e.getMessage());
                    System.err.println("SQL STATE: " + e.getSQLState());
                    throw DatabaseException.wrap("save patient data", e);
                }
            }

            case NURSE -> {
                Nurse nurse = (Nurse) person;
                String sql = """
                    INSERT INTO nurses
                    (id, ward, phone, shift)
                    VALUES (?, ?, ?, ?)
                    """;
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, nurse.getId());
                    stmt.setString(2, nurse.getWard() != null
                            ? nurse.getWard() : "General");
                    stmt.setString(3, nurse.getPhone() != null
                            ? nurse.getPhone() : "");
                    stmt.setString(4, nurse.getShift() != null
                            ? nurse.getShift().name() : "MORNING");
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    throw DatabaseException.wrap(
                            "save nurse data", e);
                }
            }
        }
    }


    public Optional<Person> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapBasePerson(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findByEmail", e);
        }
        return Optional.empty();
    }



    public Optional<Person> findById(String id) {
        String cacheKey = "user:" + id;
        String cached   = CacheService.get(cacheKey);
        if (cached != null) {
            try {
                return Optional.of(JsonUtil.fromJson(cached, Doctor.class));
            } catch (Exception ignored) {}
        }

        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Person person = mapBasePerson(rs);
                try {
                    CacheService.set(cacheKey,
                            JsonUtil.toJson(person),
                            CacheService.TTL_DOCTOR_PROFILE);
                } catch (Exception ignored) {}
                return Optional.of(person);
            }

        } catch (SQLException e) {
            throw DatabaseException.wrap("findById", e);
        }
        return Optional.empty();
    }



    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim().toLowerCase());
            return stmt.executeQuery().next();

        } catch (SQLException e) {
            throw DatabaseException.wrap("existsByEmail", e);
        }
    }



    public void updateSecurityFields(Person person) {
        String sql = """
                UPDATE users
                SET status = ?, failed_attempts = ?, locked_until = ?
                WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getStatus().name());
            stmt.setInt(2,    person.getFailedAttempts());
            stmt.setString(3, person.getLockedUntil());
            stmt.setString(4, person.getId());
            stmt.executeUpdate();
            CacheService.delete("user:" + person.getId());

        } catch (SQLException e) {
            throw DatabaseException.wrap("updateSecurityFields", e);
        }
    }



    public void updatePassword(String userId, String newHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHash);
            stmt.setString(2, userId);
            stmt.executeUpdate();

            CacheService.delete("user:" + userId);

        } catch (SQLException e) {
            throw DatabaseException.wrap("updatePassword", e);
        }
    }



    public List<Person> findAllByRole(UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ?";
        List<Person> people = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) people.add(mapBasePerson(rs));

        } catch (SQLException e) {
            throw DatabaseException.wrap("findAllByRole", e);
        }
        return people;
    }



    private Person mapBasePerson(ResultSet rs) throws SQLException {
        UserRole role = UserRole.fromString(rs.getString("role"));

        Person person = switch (role) {
            case DOCTOR, ADMIN -> new Doctor();
            case PATIENT       -> new Patient();
            case NURSE         -> new Nurse();
        };

        person.setId(rs.getString("id"));
        person.setEmail(rs.getString("email"));
        person.setUsername(rs.getString("username"));
        person.setPasswordHash(rs.getString("password_hash"));
        person.setRole(role);
        person.setStatus(UserStatus.fromString(rs.getString("status")));
        person.setFailedAttempts(rs.getInt("failed_attempts"));
        person.setLockedUntil(rs.getString("locked_until"));
        person.setCreatedAt(rs.getString("created_at"));

        return person;
    }
}