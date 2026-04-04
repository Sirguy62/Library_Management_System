package com.hospital.service;

import com.hospital.enums.AuditEventType;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.*;
import com.hospital.model.Appointment;
import com.hospital.model.AuditLog;
import com.hospital.model.Bill;
import com.hospital.store.AppointmentStore;
import com.hospital.store.AuditStore;
import com.hospital.store.BillStore;

import java.util.List;

public class BillingService {

    private static final double BASE_FEE = 15000.0; // NGN

    private final BillStore       billStore;
    private final AppointmentStore appointmentStore;
    private final AuditStore       auditStore;

    public BillingService(BillStore billStore,
                          AppointmentStore appointmentStore,
                          AuditStore auditStore) {
        this.billStore        = billStore;
        this.appointmentStore = appointmentStore;
        this.auditStore       = auditStore;
    }


    public Bill generate(String appointmentId,
                         double discountPercent,
                         String requesterId,
                         String ipAddress) {
        Appointment appointment =
                appointmentStore.findById(appointmentId)
                        .orElseThrow(() ->
                                NotFoundException.appointment(appointmentId));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new ValidationException(
                    "Bill can only be generated for completed appointments");
        }

        double amount = calculateAmount(
                appointment.getDurationMinutes());

        Bill bill = new Bill(
                appointmentId,
                appointment.getPatientId(),
                amount,
                discountPercent
        );
        billStore.save(bill);

        auditStore.log(new AuditLog(
                requesterId,
                AuditEventType.BILL_GENERATED,
                "Bill generated for appointment: " + appointmentId
                        + " — Total: " + bill.getTotal(),
                ipAddress
        ));

        return bill;
    }


    public Bill markPaid(String billId,
                         String requesterId,
                         String ipAddress) {
        Bill bill = billStore.findById(billId)
                .orElseThrow(() -> NotFoundException.bill(billId));

        if (bill.isPaid()) {
            throw new ValidationException("Bill is already paid");
        }

        bill.markPaid();
        billStore.markPaid(billId);

        auditStore.log(new AuditLog(
                requesterId,
                AuditEventType.BILL_PAID,
                "Bill paid: " + billId
                        + " — Amount: " + bill.getTotal(),
                ipAddress
        ));

        return bill;
    }


    public List<Bill> getPatientBills(String patientId) {
        return billStore.findByPatientId(patientId);
    }

    public Bill getBill(String billId) {
        return billStore.findById(billId)
                .orElseThrow(() -> NotFoundException.bill(billId));
    }


    private double calculateAmount(int durationMinutes) {
        if (durationMinutes <= 30) return BASE_FEE;
        if (durationMinutes <= 60) return BASE_FEE * 1.5;
        return BASE_FEE * 2.0;
    }
}
