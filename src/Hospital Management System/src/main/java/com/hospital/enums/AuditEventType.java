package com.hospital.enums;

public enum AuditEventType {
    REGISTER,
    EMAIL_VERIFIED,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGIN_BLOCKED,
    LOGOUT,
    LOGOUT_ALL,
    TOKEN_REFRESHED,
    TOKEN_BLACKLISTED,
    PASSWORD_RESET_REQUESTED,
    PASSWORD_RESET_COMPLETED,
    ACCOUNT_LOCKED,
    ACCOUNT_UNLOCKED,
    APPOINTMENT_BOOKED,
    APPOINTMENT_CANCELLED,
    APPOINTMENT_COMPLETED,
    BILL_GENERATED,
    BILL_PAID;

    public static AuditEventType fromString(String value) {
        for (AuditEventType type : AuditEventType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown audit event: " + value);
    }
}