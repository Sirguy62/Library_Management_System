package com.hospital.model;

import com.hospital.enums.AuditEventType;
import com.hospital.enums.BloodGroup;
import com.hospital.enums.UserRole;
import com.hospital.interfaces.Billable;
import com.hospital.model.base.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Patient extends Person implements Billable {

    private BloodGroup bloodGroup;
    private String     phone;
    private String     address;
    private String     dateOfBirth;

    private List<Bill>     bills     = new ArrayList<>();
    private List<AuditLog> auditLogs = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────

    public Patient() {}

    public Patient(String email, String username, String passwordHash,
                   BloodGroup bloodGroup, String phone,
                   String address, String dateOfBirth) {
        super(email, username, passwordHash, UserRole.PATIENT);
        this.bloodGroup  = bloodGroup;
        this.phone       = phone;
        this.address     = address;
        this.dateOfBirth = dateOfBirth;
    }

    // ── Abstract method implementations ──────────────────────

    @Override
    public String getRoleDescription() {
        return "Patient";
    }

    @Override
    public String getDisplayName() {
        return getUsername();
    }

    // ── Billable implementation ───────────────────────────────

    @Override
    public double calculateBill(double baseAmount) {
        // Base implementation — subclasses or strategies can override
        return baseAmount;
    }

    @Override
    public double applyDiscount(double amount, double discountPercent) {
        if (discountPercent <= 0) return amount;
        if (discountPercent >= 100) return 0;
        return amount - (amount * discountPercent / 100);
    }

    @Override
    public List<Bill> getBills() {
        return bills;
    }

    @Override
    public double getTotalOutstanding() {
        return bills.stream()
                .filter(b -> b.getStatus() ==
                        com.hospital.enums.BillStatus.UNPAID)
                .mapToDouble(Bill::getTotal)
                .sum();
    }


    @Override
    public void logEvent(AuditEventType eventType, String description) {
        auditLogs.add(new AuditLog(getId(), eventType, description));
    }

    @Override
    public List<AuditLog> getAuditLog() {
        return auditLogs;
    }

    public BloodGroup getBloodGroup()                      { return bloodGroup; }
    public void       setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }
    public String     getPhone()                           { return phone; }
    public void       setPhone(String phone)               { this.phone = phone; }
    public String     getAddress()                         { return address; }
    public void       setAddress(String address)           { this.address = address; }
    public String     getDateOfBirth()                     { return dateOfBirth; }
    public void       setDateOfBirth(String dateOfBirth)   { this.dateOfBirth = dateOfBirth; }
    public void       setBills(List<Bill> bills)           { this.bills = bills; }
}