package com.hospital.handler;

import com.hospital.enums.UserRole;
import com.hospital.exception.NotFoundException;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.model.base.Person;
import com.hospital.store.AppointmentStore;
import com.hospital.store.UserStore;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorHandler {

    private final UserStore        userStore;
    private final AppointmentStore appointmentStore;
    private final AuthMiddleware   authMiddleware;

    public DoctorHandler(UserStore userStore,
                         AppointmentStore appointmentStore,
                         AuthMiddleware authMiddleware) {
        this.userStore        = userStore;
        this.appointmentStore = appointmentStore;
        this.authMiddleware   = authMiddleware;
    }


    public void getAll(HttpExchange exchange) throws IOException {
        authMiddleware.authenticate(exchange);

        List<Person> doctors =
                userStore.findAllByRole(UserRole.DOCTOR);

        Map<String, Object> response = new HashMap<>();
        response.put("total",   doctors.size());
        response.put("doctors", doctors.stream()
                .map(this::safeDoctor)
                .toList());

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    public void getOne(HttpExchange exchange) throws IOException {
        authMiddleware.authenticate(exchange);

        String doctorId = extractId(
                exchange.getRequestURI().getPath());

        Person doctor = userStore.findById(doctorId)
                .orElseThrow(() ->
                        NotFoundException.doctor(doctorId));

        if (doctor.getRole() != UserRole.DOCTOR) {
            throw NotFoundException.doctor(doctorId);
        }

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(safeDoctor(doctor)));
    }


    public void getAppointments(HttpExchange exchange)
            throws IOException {
        String requesterId = authMiddleware.authenticate(exchange);
        String doctorId    = extractId(
                exchange.getRequestURI().getPath());

        UserRole requesterRole =
                authMiddleware.extractRole(exchange);
        if (!requesterId.equals(doctorId)
                && !requesterRole.isAdmin()) {
            throw com.hospital.exception.ForbiddenException
                    .notYourResource();
        }

        var appointments =
                appointmentStore.findByDoctorId(doctorId);

        Map<String, Object> response = new HashMap<>();
        response.put("doctorId",     doctorId);
        response.put("total",        appointments.size());
        response.put("appointments", appointments);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    private Map<String, Object> safeDoctor(Person person) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",          person.getId());
        map.put("username",    person.getUsername());
        map.put("email",       person.getEmail());
        map.put("role",        person.getRole().name());
        map.put("displayName", person.getDisplayName());
        map.put("description", person.getRoleDescription());
        return map;
    }

    public String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}