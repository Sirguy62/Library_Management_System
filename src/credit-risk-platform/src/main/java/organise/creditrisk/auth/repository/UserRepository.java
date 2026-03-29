package organise.creditrisk.auth.repository;

import organise.creditrisk.auth.model.User;
import organise.creditrisk.common.enums.UserRole;
import organise.creditrisk.infrastructure.db.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository extends BaseRepository {

    public User findByEmail(String email) {

        String sql = "SELECT id, email, password_hash, role FROM users WHERE email = ?";

        Connection conn = getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        UserRole.valueOf(rs.getString("role"))
                );
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(conn);
        }
    }
}