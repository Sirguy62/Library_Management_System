package com.gateway.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class LedgerEntry {

    private final String id;
    private final String accountId;
    private final double amount;
    private final LedgerDirection direction;
    private final String transactionId;
    private final LocalDateTime createdAt;

    public LedgerEntry(String accountId,
                       double amount,
                       LedgerDirection direction,
                       String transactionId) {

        this.id = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.amount = amount;
        this.direction = direction;
        this.transactionId = transactionId;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public LedgerDirection getDirection() {
        return direction;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}