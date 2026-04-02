package com.authapi.model;

import java.time.Instant;
import java.util.UUID;

public class Loan {

    private String id;
    private String userId;
    private String borrowerName;
    private double amount;
    private double collateral;
    private double amountPaid;
    private String status;
    private String dueDate;
    private String createdAt;

    public Loan() {}

    public Loan(String userId, String borrowerName, double amount, double collateral, String dueDate) {
        this.userId = userId;
        this.borrowerName = borrowerName;
        this.amount = amount;
        this.collateral = collateral;
        this.dueDate = dueDate;
        this.status = "ACTIVE";
        this.amountPaid = 0.0;
        this.createdAt = Instant.now().toString();
        this.id = UUID.randomUUID().toString();
    }

    public String getId()                   { return id; }
    public void   setId(String id)          { this.id = id; }

    public String getUserId()               { return userId; }
    public void   setUserId(String userId)  { this.userId = userId; }

    public String getBorrowerName()                    { return borrowerName; }
    public void   setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public double getAmount()                 { return amount; }
    public void   setAmount(double amount)    { this.amount = amount; }

    public double getCollateral()                    { return collateral; }
    public void   setCollateral(double collateral)   { this.collateral = collateral; }

    public double getAmountPaid()                    { return amountPaid; }
    public void   setAmountPaid(double amountPaid)   { this.amountPaid = amountPaid; }

    public String getStatus()                  { return status; }
    public void   setStatus(String status)     { this.status = status; }

    public String getDueDate()                 { return dueDate; }
    public void   setDueDate(String dueDate)   { this.dueDate = dueDate; }

    public String getCreatedAt()                   { return createdAt; }
    public void   setCreatedAt(String createdAt)   { this.createdAt = createdAt; }

    public double getRemainingBalance() {
        return amount - amountPaid;
    }

    public boolean isFullyPaid() {
        return amountPaid >= amount;
    }
}
