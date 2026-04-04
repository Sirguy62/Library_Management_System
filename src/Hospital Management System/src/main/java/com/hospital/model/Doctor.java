package com.hospital.model;

import com.hospital.enums.AuditEventType;
import com.hospital.enums.Specialization;
import com.hospital.enums.UserRole;
import com.hospital.interfaces.Schedulable;
import com.hospital.model.base.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Doctor extends Person implements Schedulable {

    private Specialization specialization;
    private String         licenseNumber;
    private String         phone;
    private String         availableFrom;
    private String         availableTo;

    // Held in memory — loaded from DB when needed
    private List<Appointment> appointments = new ArrayList<>();
    private List<AuditLog>    auditLogs    = new ArrayList<>();

    public Doctor() {}

    public Doctor(String email, String username, String passwordHash,
                  Specialization specialization, String licenseNumber,
                  String phone) {
        super(email, username, passwordHash, UserRole.DOCTOR);
        this.specialization = specialization;
        this.licenseNumber  = licenseNumber;
        this.phone          = phone;
        this.availableFrom  = "08:00";
        this.availableTo    = "17:00";
    }

    @Override
    public String getRoleDescription() {
        return "Doctor — " + (specialization != null
                ? specialization.getDisplay()
                : "General Practice");
    }

    @Override
    public String getDisplayName() {
        return "Dr. " + getUsername();
    }

    @Override
    public List<Appointment> getSchedule() {
        return appointments.stream()
                .filter(a -> a.getStatus() == com.hospital.enums.AppointmentStatus.SCHEDULED)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailableAt(String scheduledAt, int durationMinutes) {
        // Check no existing appointment overlaps this time slot
        return appointments.stream()
                .filter(a -> a.getStatus() == com.hospital.enums.AppointmentStatus.SCHEDULED)
                .noneMatch(a -> overlaps(a, scheduledAt, durationMinutes));
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    @Override
    public List<Appointment> getScheduleForDate(String date) {
        return appointments.stream()
                .filter(a -> a.getScheduledAt().startsWith(date))
                .collect(Collectors.toList());
    }

    @Override
    public void logEvent(AuditEventType eventType, String description) {
        auditLogs.add(new AuditLog(getId(), eventType, description));
    }

    @Override
    public List<AuditLog> getAuditLog() {
        return auditLogs;
    }

    private boolean overlaps(Appointment existing,
                             String newStart, int newDuration) {
        java.time.Instant eStart = java.time.Instant.parse(existing.getScheduledAt());
        java.time.Instant eEnd   = eStart.plusSeconds(
                existing.getDurationMinutes() * 60L);
        java.time.Instant nStart = java.time.Instant.parse(newStart);
        java.time.Instant nEnd   = nStart.plusSeconds(newDuration * 60L);

        return nStart.isBefore(eEnd) && nEnd.isAfter(eStart);
    }


    public Specialization getSpecialization()                        { return specialization; }
    public void           setSpecialization(Specialization s)        { this.specialization = s; }
    public String         getLicenseNumber()                         { return licenseNumber; }
    public void           setLicenseNumber(String licenseNumber)     { this.licenseNumber = licenseNumber; }
    public String         getPhone()                                  { return phone; }
    public void           setPhone(String phone)                      { this.phone = phone; }
    public String         getAvailableFrom()                          { return availableFrom; }
    public void           setAvailableFrom(String availableFrom)      { this.availableFrom = availableFrom; }
    public String         getAvailableTo()                            { return availableTo; }
    public void           setAvailableTo(String availableTo)          { this.availableTo = availableTo; }
}