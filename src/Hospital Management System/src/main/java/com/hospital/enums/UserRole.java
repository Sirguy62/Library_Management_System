package com.hospital.enums;

public enum UserRole {
    DOCTOR,
    PATIENT,
    NURSE,
    ADMIN;

    public static UserRole fromString(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isMedicalStaff() {
        return this == DOCTOR || this == NURSE;
    }
}