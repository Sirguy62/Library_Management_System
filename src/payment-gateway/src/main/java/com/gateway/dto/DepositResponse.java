package com.gateway.dto;

import com.gateway.model.TransactionStatus;

public class DepositResponse {

    public String transactionId;
    public TransactionStatus status;
    public double newBalance;

    public DepositResponse(String transactionId,
                           TransactionStatus status,
                           double newBalance) {
        this.transactionId = transactionId;
        this.status = status;
        this.newBalance = newBalance;
    }
}