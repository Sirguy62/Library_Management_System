package com.hospital.model;

import com.hospital.enums.AppointmentStatus;

import java.time.Instant;
import java.util.UUID;

public class Appointment {

    private String            id;
    private String            doctorId;
    private String            patientId;
    private String            scheduledAt;
    private int               durationMinutes;
    private AppointmentStatus status;
    private String            notes;
    private String            createdAt;

    public Appointment() {}

    public Appointment(String doctorId, String patientId,
                       String scheduledAt, int durationMinutes,
                       String notes) {
        this.id              = UUID.randomUUID().toString();
        this.doctorId        = doctorId;
        this.patientId       = patientId;
        this.scheduledAt     = scheduledAt;
        this.durationMinutes = durationMinutes;
        this.status          = AppointmentStatus.SCHEDULED;
        this.notes           = notes;
        this.createdAt       = Instant.now().toString();
    }

    public boolean isActive() {
        return status == AppointmentStatus.SCHEDULED;
    }

    public boolean isCancellable() {
        return status == AppointmentStatus.SCHEDULED;
    }

    public String            getId()                              { return id; }
    public void              setId(String id)                     { this.id = id; }
    public String            getDoctorId()                        { return doctorId; }
    public void              setDoctorId(String doctorId)         { this.doctorId = doctorId; }
    public String            getPatientId()                       { return patientId; }
    public void              setPatientId(String patientId)       { this.patientId = patientId; }
    public String            getScheduledAt()                     { return scheduledAt; }
    public void              setScheduledAt(String scheduledAt)   { this.scheduledAt = scheduledAt; }
    public int               getDurationMinutes()                 { return durationMinutes; }
    public void              setDurationMinutes(int d)            { this.durationMinutes = d; }
    public AppointmentStatus getStatus()                          { return status; }
    public void              setStatus(AppointmentStatus status)  { this.status = status; }
    public String            getNotes()                           { return notes; }
    public void              setNotes(String notes)               { this.notes = notes; }
    public String            getCreatedAt()                       { return createdAt; }
    public void              setCreatedAt(String createdAt)       { this.createdAt = createdAt; }
}