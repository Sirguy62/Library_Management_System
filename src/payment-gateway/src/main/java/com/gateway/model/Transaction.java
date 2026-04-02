package com.gateway.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private String id;
    private String accountId;
    private double amount;
    private TransactionStatus status;
    private TransactionType type;
    private String idempotencyKey;
    private LocalDateTime createdAt;

    public Transaction(String accountId,
                       double amount,
                       String idempotencyKey,
                       TransactionType type) {

        this.id = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.type = type;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void markSuccess() {
        this.status = TransactionStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = TransactionStatus.FAILED;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public TransactionType getType() {
        return type;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}