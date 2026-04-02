package com.authapi.store;

import com.authapi.db.DatabaseConnection;
import com.authapi.model.AuthToken;
import com.authapi.model.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserStore {

    private final Map<String, User> usersByEmail = new HashMap<>();
    private final Map<String, AuthToken> tokenStore = new HashMap<>();


    public User save (User user) {
        String sql = "INSERT INTO users (id, email, username, password_hash, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try(Connection connect = DatabaseConnection.getConnection()){
            PreparedStatement prep = connect.prepareStatement(sql); {

                prep.setString(1, user.getId());
                prep.setString(2, user.getEmail().trim().toLowerCase());
                prep.setString(3, user.getUsername());
                prep.setString(4, user.getPasswordHash());
                prep.setString(5, user.getCreatedAt());
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
        return  user;
    }

    public Optional<User> findByEmail(String email) {
       String sql = "SELECT * FROM users WHERE email = ?";
       try (Connection connect = DatabaseConnection.getConnection();
           PreparedStatement prep = connect.prepareStatement(sql)){

                   prep.setString(1, email.trim().toLowerCase());
           ResultSet res = prep.executeQuery();

           if (res.next()) {
               return Optional.of(mapUser(res));
           }
       } catch (SQLException e) {
           throw new RuntimeException("Failed to find user: " + e.getMessage(), e);
       }
       return Optional.empty();
    }

    public Optional<User> findById(String id) {
      String sql = "SELECT * FROM users WHERE id = ?";
      try (Connection connect = DatabaseConnection.getConnection();
          PreparedStatement prep = connect.prepareStatement(sql)) {

          prep.setString(1, id);
          ResultSet res = prep.executeQuery();

          if(res.next()) {
              return Optional.of(mapUser(res));
          }
      } catch (SQLException e) {
          throw new RuntimeException("Failed to find user: " + e.getMessage(), e);
      }
      return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try(Connection connect = DatabaseConnection.getConnection();
        PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, email.trim().toLowerCase());
            ResultSet res = prep.executeQuery();
            return res.next();
        }catch (SQLException e) {
            throw new RuntimeException("Failed to check email: " + e.getMessage(), e);
        }
    }

    public void deleteByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection connect = DatabaseConnection.getConnection();
           PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, email.trim().toLowerCase());
            prep.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    public void update (User updatedUser) {
        String sql = "UPDATE usres SET Username = ?, password_hash = ? WHERE id = ?";
        try (Connection connect = DatabaseConnection.getConnection();
           PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, updatedUser.getUsername());
            prep.setString(2, updatedUser.getPasswordHash());
            prep.setString(3, updatedUser.getId());
            prep.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public void saveToken(AuthToken token) {
       String sql = "INSERT INTO auth_tokens (token, user_id, expires_at) VALUES (?, ?, ?)";
       try (Connection connect = DatabaseConnection.getConnection();
         PreparedStatement prep = connect.prepareStatement(sql)) {

           prep.setString(1, token.getToken());
           prep.setString(2, token.getUserId());
           prep.setString(3, token.getExpiresAt());
           prep.executeUpdate();

       }catch (SQLException e) {
           throw new RuntimeException("Failed to save token: " + e.getMessage(), e);
       }
    }

    public Optional<AuthToken> findToken(String token) {
        String sql = "SELECT * FROM auth_tokens WHERE token = ?";
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, token);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                AuthToken tok = new AuthToken();
                tok.setToken(res.getString("token"));
                tok.setUserId(res.getString("user_id"));
                tok.setExpiresAt(res.getString("expires_at"));
                return Optional.of(tok);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find token: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void deleteToken(String token) {
        String sql = "DELETE FROM auth_tokens WHERE token = ?";
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement prep = connect.prepareStatement(sql)) {

            prep.setString(1, token);
            prep.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("Failed to delete token: " + e.getMessage(), e);
        }
    }


    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setCreatedAt(rs.getString("created_at"));
        return user;
    }
}









