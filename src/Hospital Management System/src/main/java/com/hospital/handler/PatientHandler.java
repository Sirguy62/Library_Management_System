package com.hospital.handler;

import com.hospital.enums.UserRole;
import com.hospital.exception.ForbiddenException;
import com.hospital.exception.NotFoundException;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.model.base.Person;
import com.hospital.service.BillingService;
import com.hospital.store.AppointmentStore;
import com.hospital.store.UserStore;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PatientHandler {

    private final UserStore        userStore;
    private final AppointmentStore appointmentStore;
    private final BillingService   billingService;
    private final AuthMiddleware   authMiddleware;

    public PatientHandler(UserStore userStore,
                          AppointmentStore appointmentStore,
                          BillingService billingService,
                          AuthMiddleware authMiddleware) {
        this.userStore        = userStore;
        this.appointmentStore = appointmentStore;
        this.billingService   = billingService;
        this.authMiddleware   = authMiddleware;
    }


    public void getOne(HttpExchange exchange) throws IOException {
        String requesterId = authMiddleware.authenticate(exchange);
        String patientId   = extractId(
                exchange.getRequestURI().getPath());

        Person patient = userStore.findById(patientId)
                .orElseThrow(() ->
                        NotFoundException.patient(patientId));

        UserRole role = authMiddleware.extractRole(exchange);
        if (!requesterId.equals(patientId)
                && role != UserRole.DOCTOR
                && !role.isAdmin()) {
            throw ForbiddenException.notYourResource();
        }

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(safePatient(patient)));
    }


    public void getAppointments(HttpExchange exchange)
            throws IOException {
        String requesterId = authMiddleware.authenticate(exchange);
        String patientId   = extractId(
                exchange.getRequestURI().getPath());

        UserRole role = authMiddleware.extractRole(exchange);
        if (!requesterId.equals(patientId)
                && role != UserRole.DOCTOR
                && !role.isAdmin()) {
            throw ForbiddenException.notYourResource();
        }

        var appointments =
                appointmentStore.findByPatientId(patientId);

        Map<String, Object> response = new HashMap<>();
        response.put("patientId",    patientId);
        response.put("total",        appointments.size());
        response.put("appointments", appointments);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    public void getBills(HttpExchange exchange) throws IOException {
        String requesterId = authMiddleware.authenticate(exchange);
        String patientId   = extractId(
                exchange.getRequestURI().getPath());

        UserRole role = authMiddleware.extractRole(exchange);
        if (!requesterId.equals(patientId)
                && !role.isAdmin()) {
            throw ForbiddenException.notYourResource();
        }

        var bills = billingService.getPatientBills(patientId);

        double outstanding = bills.stream()
                .filter(b -> !b.isPaid())
                .mapToDouble(b -> b.getTotal())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("patientId",    patientId);
        response.put("total",        bills.size());
        response.put("outstanding",  outstanding);
        response.put("bills",        bills);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    private Map<String, Object> safePatient(Person person) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",          person.getId());
        map.put("username",    person.getUsername());
        map.put("email",       person.getEmail());
        map.put("role",        person.getRole().name());
        map.put("displayName", person.getDisplayName());
        return map;
    }

    public String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}