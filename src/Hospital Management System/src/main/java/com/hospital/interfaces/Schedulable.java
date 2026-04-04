package com.hospital.interfaces;

import com.hospital.model.Appointment;
import java.util.List;

public interface Schedulable {

    List<Appointment> getSchedule();

    boolean isAvailableAt(String scheduledAt, int durationMinutes);

    void addAppointment(Appointment appointment);

    List<Appointment> getScheduleForDate(String date);
}