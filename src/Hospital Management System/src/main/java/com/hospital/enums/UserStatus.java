package com.hospital.enums;

public enum UserStatus {
    UNVERIFIED,
    VERIFIED,
    LOCKED;

    public static UserStatus fromString(String value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }

    public boolean canLogin() {
        return this == VERIFIED;
    }
}