package com.gateway.dao;

import com.gateway.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountRepository {

    public void save(Account account) {

        try (Connection con =
                     com.gateway.config.DatabaseConfig.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO accounts (id,name,balance,created_at) VALUES (?,?,?,?)"
            );

            ps.setString(1, account.getId());
            ps.setString(2, account.getName());
            ps.setDouble(3, account.getBalance());
            ps.setObject(4, account.getCreatedAt());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account findForUpdate(Connection con, String id) {

        try {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE id=? FOR UPDATE"
            );

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Account(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getDouble("balance"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(Connection con,
                              String id,
                              double balance) {

        try {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance=? WHERE id=?"
            );

            ps.setDouble(1, balance);
            ps.setString(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account findById(String id) {

        try (java.sql.Connection con =
                     com.gateway.config.DatabaseConfig.getConnection()) {

            java.sql.PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT * FROM accounts WHERE id=?"
                    );

            ps.setString(1, id);

            java.sql.ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Account(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getDouble("balance"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}