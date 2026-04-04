package com.hospital.enums;

public enum BloodGroup {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-");

    private final String display;

    BloodGroup(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static BloodGroup fromString(String value) {
        for (BloodGroup bg : BloodGroup.values()) {
            if (bg.display.equalsIgnoreCase(value) ||
                    bg.name().equalsIgnoreCase(value)) {
                return bg;
            }
        }
        throw new IllegalArgumentException("Unknown blood group: " + value);
    }
}