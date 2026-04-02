package com.gateway.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Account {

    private String id;
    private String name;
    private double balance;
    private LocalDateTime createdAt;

    // API constructor
    public Account(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.balance = 0;
        this.createdAt = LocalDateTime.now();
    }

    // DB reconstruction constructor
    public Account(String id,
                   String name,
                   double balance,
                   LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public boolean withdraw(double amount) {
        if (balance < amount) return false;
        balance -= amount;
        return true;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}