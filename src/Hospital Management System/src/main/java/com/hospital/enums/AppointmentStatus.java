package com.hospital.enums;

public enum AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED;

    public static AppointmentStatus fromString(String value) {
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown appointment status: " + value);
    }

    public boolean isActive() {
        return this == SCHEDULED;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }
}