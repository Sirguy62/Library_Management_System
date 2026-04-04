package com.hospital.model;

import com.hospital.enums.BillStatus;

import java.time.Instant;
import java.util.UUID;

public class Bill {

    private String     id;
    private String     appointmentId;
    private String     patientId;
    private double     amount;
    private double     discount;
    private double     total;
    private BillStatus status;
    private String     createdAt;

    public Bill() {}

    public Bill(String appointmentId, String patientId,
                double amount, double discountPercent) {
        this.id            = UUID.randomUUID().toString();
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.amount        = amount;
        this.discount      = amount * discountPercent / 100;
        this.total         = amount - this.discount;
        this.status        = BillStatus.UNPAID;
        this.createdAt     = Instant.now().toString();
    }

    public void markPaid() {
        this.status = BillStatus.PAID;
    }

    public boolean isPaid() {
        return status == BillStatus.PAID;
    }

    public String     getId()                            { return id; }
    public void       setId(String id)                   { this.id = id; }
    public String     getAppointmentId()                 { return appointmentId; }
    public void       setAppointmentId(String a)         { this.appointmentId = a; }
    public String     getPatientId()                     { return patientId; }
    public void       setPatientId(String patientId)     { this.patientId = patientId; }
    public double     getAmount()                        { return amount; }
    public void       setAmount(double amount)           { this.amount = amount; }
    public double     getDiscount()                      { return discount; }
    public void       setDiscount(double discount)       { this.discount = discount; }
    public double     getTotal()                         { return total; }
    public void       setTotal(double total)             { this.total = total; }
    public BillStatus getStatus()                        { return status; }
    public void       setStatus(BillStatus status)       { this.status = status; }
    public String     getCreatedAt()                     { return createdAt; }
    public void       setCreatedAt(String createdAt)     { this.createdAt = createdAt; }
}