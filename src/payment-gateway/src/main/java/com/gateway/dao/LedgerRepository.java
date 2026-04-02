package com.gateway.dao;

import com.gateway.model.LedgerDirection;
import com.gateway.model.LedgerEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LedgerRepository {

    public void save(Connection con, LedgerEntry entry) {

        try {
            PreparedStatement ps =
                    con.prepareStatement(
                            // this process records financiaal event
                            "INSERT INTO ledger_entries " +
                                    "(id, account_id, amount, direction, transaction_id, created_at) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)"
                    );

            // This uniquely identifies this financial movement
            ps.setString(1, entry.getId());
            // This groups all financial events belonging to same wallet
            ps.setString(2, entry.getAccountId());
            //
            ps.setDouble(3, entry.getAmount());
            // direction on if it is a credit or debit, +amount or -amount
            ps.setString(4, entry.getDirection().name());
            // transaction id
            ps.setString(5, entry.getTransactionId());
            // time stamp, time created
            ps.setObject(6, entry.getCreatedAt());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double calculateBalance(Connection con, String accountId) {

        try {
            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT " +
                                    "COALESCE(SUM(CASE WHEN direction='CREDIT' THEN amount ELSE 0 END),0) - " +
                                    "COALESCE(SUM(CASE WHEN direction='DEBIT' THEN amount ELSE 0 END),0) " +
                                    "AS balance " +
                                    "FROM ledger_entries WHERE account_id=?"
                    );

            ps.setString(1, accountId);

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getDouble("balance");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}