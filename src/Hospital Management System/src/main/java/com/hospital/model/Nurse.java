package com.hospital.model;

import com.hospital.enums.AuditEventType;
import com.hospital.enums.Shift;
import com.hospital.enums.UserRole;
import com.hospital.interfaces.Schedulable;
import com.hospital.model.base.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Nurse extends Person implements Schedulable {

    private String     ward;
    private String     phone;
    private Shift      shift;

    private List<Appointment> appointments = new ArrayList<>();
    private List<AuditLog>    auditLogs    = new ArrayList<>();

    public Nurse() {}

    public Nurse(String email, String username, String passwordHash,
                 String ward, String phone, Shift shift) {
        super(email, username, passwordHash, UserRole.NURSE);
        this.ward  = ward;
        this.phone = phone;
        this.shift = shift;
    }

    @Override
    public String getRoleDescription() {
        return "Nurse — " + (ward != null ? ward + " Ward" : "Unassigned");
    }

    @Override
    public String getDisplayName() {
        return "Nurse " + getUsername();
    }

    @Override
    public List<Appointment> getSchedule() {
        return appointments.stream()
                .filter(a -> a.getStatus() ==
                        com.hospital.enums.AppointmentStatus.SCHEDULED)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailableAt(String scheduledAt, int durationMinutes) {
        return true; // Nurses are available unless on leave
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


    public String getWard()                  { return ward; }
    public void   setWard(String ward)       { this.ward = ward; }
    public String getPhone()                 { return phone; }
    public void   setPhone(String phone)     { this.phone = phone; }
    public Shift  getShift()                 { return shift; }
    public void   setShift(Shift shift)      { this.shift = shift; }
}