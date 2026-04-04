package com.hospital.exception;

public class ConflictException extends AppException {

    public ConflictException(String message) {
        super(409, "CONFLICT", message);
    }

    public static ConflictException emailTaken() {
        return new ConflictException("Email is already registered");
    }

    public static ConflictException appointmentConflict() {
        return new ConflictException(
                "Doctor already has an appointment at this time");
    }

    public static ConflictException alreadyVerified() {
        return new ConflictException("Email is already verified");
    }
}