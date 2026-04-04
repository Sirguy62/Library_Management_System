package com.hospital.service;

import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.AuditEventType;
import com.hospital.enums.UserRole;
import com.hospital.exception.*;
import com.hospital.model.Appointment;
import com.hospital.model.AuditLog;
import com.hospital.model.base.Person;
import com.hospital.store.AppointmentStore;
import com.hospital.store.AuditStore;
import com.hospital.store.UserStore;

import java.util.List;

public class AppointmentService {

    private final AppointmentStore appointmentStore;
    private final UserStore        userStore;
    private final AuditStore       auditStore;

    public AppointmentService(AppointmentStore appointmentStore,
                              UserStore userStore,
                              AuditStore auditStore) {
        this.appointmentStore = appointmentStore;
        this.userStore        = userStore;
        this.auditStore       = auditStore;
    }

    public Appointment book(String doctorId,
                            String patientId,
                            String scheduledAt,
                            int durationMinutes,
                            String notes,
                            String ipAddress) {
        Person doctor = userStore.findById(doctorId)
                .orElseThrow(() -> NotFoundException.doctor(doctorId));

        if (doctor.getRole() != UserRole.DOCTOR) {
            throw new ValidationException(
                    "The specified user is not a doctor");
        }

        Person patient = userStore.findById(patientId)
                .orElseThrow(() -> NotFoundException.patient(patientId));

        try {
            java.time.Instant scheduled =
                    java.time.Instant.parse(scheduledAt);
            if (scheduled.isBefore(java.time.Instant.now())) {
                throw new ValidationException(
                        "Appointment must be scheduled in the future");
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new ValidationException(
                    "Invalid date format. Use ISO format: "
                            + "2024-01-15T10:00:00Z");
        }

        List<Appointment> existing =
                appointmentStore.findByDoctorId(doctorId);
        boolean conflict = existing.stream()
                .filter(Appointment::isActive)
                .anyMatch(a -> overlaps(
                        a, scheduledAt, durationMinutes));

        if (conflict) {
            throw ConflictException.appointmentConflict();
        }

        Appointment appointment = new Appointment(
                doctorId, patientId,
                scheduledAt, durationMinutes, notes);
        appointmentStore.save(appointment);

        EmailService.sendAppointmentConfirmation(
                patient,
                doctor.getDisplayName(),
                scheduledAt
        );

        auditStore.log(new AuditLog(
                patientId,
                AuditEventType.APPOINTMENT_BOOKED,
                "Appointment booked with " + doctor.getDisplayName(),
                ipAddress
        ));

        return appointment;
    }


    public Appointment complete(String appointmentId,
                                String doctorId,
                                String ipAddress) {
        Appointment appointment = findAndValidate(
                appointmentId, doctorId);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentStore.update(appointment);

        auditStore.log(new AuditLog(
                doctorId,
                AuditEventType.APPOINTMENT_COMPLETED,
                "Appointment completed: " + appointmentId,
                ipAddress
        ));

        return appointment;
    }


    public Appointment cancel(String appointmentId,
                              String requesterId,
                              String ipAddress) {
        Appointment appointment =
                appointmentStore.findById(appointmentId)
                        .orElseThrow(() ->
                                NotFoundException.appointment(appointmentId));

        if (!appointment.getPatientId().equals(requesterId)) {
            Person requester = userStore.findById(requesterId)
                    .orElseThrow(() ->
                            NotFoundException.user(requesterId));
            if (!requester.getRole().isAdmin()) {
                throw ForbiddenException.notYourResource();
            }
        }

        if (!appointment.isCancellable()) {
            throw new ValidationException(
                    "Only scheduled appointments can be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentStore.update(appointment);

        auditStore.log(new AuditLog(
                requesterId,
                AuditEventType.APPOINTMENT_CANCELLED,
                "Appointment cancelled: " + appointmentId,
                ipAddress
        ));

        return appointment;
    }


    public List<Appointment> getForDoctor(String doctorId) {
        return appointmentStore.findByDoctorId(doctorId);
    }

    public List<Appointment> getForPatient(String patientId) {
        return appointmentStore.findByPatientId(patientId);
    }


    private Appointment findAndValidate(String appointmentId,
                                        String doctorId) {
        Appointment appointment =
                appointmentStore.findById(appointmentId)
                        .orElseThrow(() ->
                                NotFoundException.appointment(appointmentId));

        if (!appointment.getDoctorId().equals(doctorId)) {
            throw ForbiddenException.notYourResource();
        }

        if (!appointment.isActive()) {
            throw new ValidationException(
                    "Appointment is not in SCHEDULED status");
        }

        return appointment;
    }

    private boolean overlaps(Appointment existing,
                             String newStart,
                             int newDuration) {
        java.time.Instant eStart =
                java.time.Instant.parse(existing.getScheduledAt());
        java.time.Instant eEnd   =
                eStart.plusSeconds(existing.getDurationMinutes() * 60L);
        java.time.Instant nStart =
                java.time.Instant.parse(newStart);
        java.time.Instant nEnd   =
                nStart.plusSeconds(newDuration * 60L);

        return nStart.isBefore(eEnd) && nEnd.isAfter(eStart);
    }
}