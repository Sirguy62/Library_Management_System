package com.gateway.dto;

public class PaymentRequest {
    public String accountId;
    public double amount;
    public String idempotencyKey;
}