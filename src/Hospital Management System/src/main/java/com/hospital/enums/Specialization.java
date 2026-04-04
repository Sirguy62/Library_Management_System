package com.hospital.enums;

public enum Specialization {
    GENERAL_PRACTICE("General Practice"),
    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    PEDIATRICS("Pediatrics"),
    ORTHOPEDICS("Orthopedics"),
    DERMATOLOGY("Dermatology"),
    PSYCHIATRY("Psychiatry"),
    ONCOLOGY("Oncology"),
    RADIOLOGY("Radiology"),
    SURGERY("Surgery");

    private final String display;

    Specialization(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static Specialization fromString(String value) {
        for (Specialization spec : Specialization.values()) {
            if (spec.display.equalsIgnoreCase(value) ||
                    spec.name().equalsIgnoreCase(value)) {
                return spec;
            }
        }
        throw new IllegalArgumentException("Unknown specialization: " + value);
    }
}