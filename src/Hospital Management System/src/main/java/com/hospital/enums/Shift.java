package com.hospital.enums;

public enum Shift {
    MORNING("06:00", "14:00"),
    AFTERNOON("14:00", "22:00"),
    NIGHT("22:00", "06:00");

    private final String startTime;
    private final String endTime;

    Shift(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    public String getStartTime() { return startTime; }
    public String getEndTime()   { return endTime; }

    public static Shift fromString(String value) {
        for (Shift shift : Shift.values()) {
            if (shift.name().equalsIgnoreCase(value)) {
                return shift;
            }
        }
        throw new IllegalArgumentException("Unknown shift: " + value);
    }
}