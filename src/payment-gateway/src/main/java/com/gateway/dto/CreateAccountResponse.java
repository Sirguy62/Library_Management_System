package com.gateway.dto;

public class CreateAccountResponse {
    public String accountId;
    public String name;
    public double balance;

    public CreateAccountResponse(String accountId, String name, double balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
    }
}
