package com.hospital.enums;

public enum BillStatus {
    UNPAID,
    PAID;

    public static BillStatus fromString(String value) {
        for (BillStatus status : BillStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown bill status: " + value);
    }

    public boolean isPaid() {
        return this == PAID;
    }
}