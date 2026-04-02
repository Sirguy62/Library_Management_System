package com.gateway.dto;

import com.gateway.model.TransactionStatus;

public class PaymentResponse {

    public String transactionId;
    public TransactionStatus status;

    public PaymentResponse(String transactionId, TransactionStatus status) {
        this.transactionId = transactionId;
        this.status = status;
    }
}