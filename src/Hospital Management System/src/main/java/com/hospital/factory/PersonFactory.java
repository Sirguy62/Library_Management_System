package com.hospital.factory;

import com.hospital.enums.BloodGroup;
import com.hospital.enums.Shift;
import com.hospital.enums.Specialization;
import com.hospital.enums.UserRole;
import com.hospital.exception.ValidationException;
import com.hospital.model.Doctor;
import com.hospital.model.Nurse;
import com.hospital.model.Patient;
import com.hospital.model.base.Person;

import java.util.Map;

public class PersonFactory {

    private PersonFactory() {}


    public static Person create(UserRole role,
                                String email,
                                String username,
                                String passwordHash,
                                Map<String, Object> extra) {
        return switch (role) {
            case DOCTOR  -> createDoctor(email, username, passwordHash, extra);
            case PATIENT -> createPatient(email, username, passwordHash, extra);
            case NURSE   -> createNurse(email, username, passwordHash, extra);
            case ADMIN   -> createAdmin(email, username, passwordHash);
        };
    }

    private static Doctor createDoctor(String email, String username,
                                       String passwordHash,
                                       Map<String, Object> extra) {
        String specStr = (String) extra.get("specialization");
        String license = (String) extra.get("licenseNumber");
        String phone   = (String) extra.getOrDefault("phone", "");

        if (specStr == null || specStr.isBlank()) {
            throw new ValidationException("Specialization is required for doctors");
        }
        if (license == null || license.isBlank()) {
            throw new ValidationException("License number is required for doctors");
        }

        Specialization spec;
        try {
            spec = Specialization.fromString(specStr);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid specialization: " + specStr);
        }

        return new Doctor(email, username, passwordHash, spec, license, phone);
    }


    private static Patient createPatient(String email, String username,
                                         String passwordHash,
                                         Map<String, Object> extra) {
        String phone       = (String) extra.getOrDefault("phone", "");
        String address     = (String) extra.getOrDefault("address", "");
        String dob         = (String) extra.getOrDefault("dateOfBirth", "");
        String bgStr       = (String) extra.getOrDefault("bloodGroup", "");

        BloodGroup bloodGroup = null;
        if (bgStr != null && !bgStr.isBlank()) {
            try {
                bloodGroup = BloodGroup.fromString(bgStr);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid blood group: " + bgStr);
            }
        }

        return new Patient(email, username, passwordHash,
                bloodGroup, phone, address, dob);
    }


    private static Nurse createNurse(String email, String username,
                                     String passwordHash,
                                     Map<String, Object> extra) {
        String ward     = (String) extra.getOrDefault("ward", "General");
        String phone    = (String) extra.getOrDefault("phone", "");
        String shiftStr = (String) extra.getOrDefault("shift", "MORNING");

        Shift shift;
        try {
            shift = Shift.fromString(shiftStr);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid shift: " + shiftStr);
        }

        return new Nurse(email, username, passwordHash, ward, phone, shift);
    }


    private static Person createAdmin(String email, String username,
                                      String passwordHash) {
        // Admin is a special Doctor with ADMIN role
        Doctor admin = new Doctor(email, username, passwordHash,
                Specialization.GENERAL_PRACTICE, "ADMIN", "");
        admin.setRole(UserRole.ADMIN);
        return admin;
    }
}