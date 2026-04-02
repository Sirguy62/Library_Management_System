package com.authapi.model;

import java.time.Instant;
import java.util.UUID;

public class Payment {

    private String id;
    private String userId;
    private double amount;
    private String note;
    private String paidAt;
    private String loanId;

    public Payment() {}

    public Payment(String userId, double amount, String note, String loanId) {
        this.userId = userId;
        this.amount = amount;
        this.note = note;
        this.loanId = loanId;
        this.id = UUID.randomUUID().toString();
        this.paidAt = Instant.now().toString();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getPaidAt() {
        return paidAt;
    }
    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getLoanId() {
        return loanId;
    }
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }



}





















