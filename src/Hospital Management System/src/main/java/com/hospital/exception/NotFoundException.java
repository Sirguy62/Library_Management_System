package com.hospital.exception;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(404, "NOT_FOUND", message);
    }

    // Factory methods per resource type
    public static NotFoundException doctor(String id) {
        return new NotFoundException("Doctor not found with id: " + id);
    }

    public static NotFoundException patient(String id) {
        return new NotFoundException("Patient not found with id: " + id);
    }

    public static NotFoundException nurse(String id) {
        return new NotFoundException("Nurse not found with id: " + id);
    }

    public static NotFoundException user(String id) {
        return new NotFoundException("User not found with id: " + id);
    }

    public static NotFoundException appointment(String id) {
        return new NotFoundException("Appointment not found with id: " + id);
    }

    public static NotFoundException bill(String id) {
        return new NotFoundException("Bill not found with id: " + id);
    }
}