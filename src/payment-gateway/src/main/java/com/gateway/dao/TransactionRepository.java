package com.gateway.dao;

import com.gateway.config.DatabaseConfig;
import com.gateway.model.Transaction;
import com.gateway.model.TransactionType;
import com.gateway.model.TransactionStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// This class talks directly to DB table transaction

public class TransactionRepository {

    public void save(Connection con, Transaction tx) {

        try {

            PreparedStatement ps = con.prepareStatement(
                    // method to INSERT INTO transactions table
                    "INSERT INTO transactions (id,account_id,amount,status,type,idempotency_key,created_at) VALUES (?,?,?,?,?,?,?)"
            );

            // Transaction primary key
            ps.setString(1, tx.getId());
            // Which wallet initiated payment
            ps.setString(2, tx.getAccountId());
            // Intent amount
            ps.setDouble(3, tx.getAmount());
            // status if pending, failed or successful
            ps.setString(4, tx.getStatus().name());
            // type of transaction, debit or credit
            ps.setString(5, tx.getType().name());
            //Retry safety key
            ps.setString(6, tx.getIdempotencyKey());
            //time of transaction
            ps.setObject(7, tx.getCreatedAt());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction findByIdempotencyKey(String key) {

        try (Connection con = DatabaseConfig.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE idempotency_key=?"
            );

            ps.setString(1, key);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Transaction(
                    rs.getString("account_id"),
                    rs.getDouble("amount"),
                    rs.getString("idempotency_key"),
                    TransactionType.valueOf(rs.getString("type"))
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}