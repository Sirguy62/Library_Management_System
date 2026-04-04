package com.hospital.handler;

import com.hospital.enums.UserRole;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.service.AppointmentService;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppointmentHandler {

    private final AppointmentService appointmentService;
    private final AuthMiddleware     authMiddleware;

    public AppointmentHandler(AppointmentService appointmentService,
                              AuthMiddleware authMiddleware) {
        this.appointmentService = appointmentService;
        this.authMiddleware     = authMiddleware;
    }


    public void book(HttpExchange exchange) throws IOException {
        // Only patients can book
        String patientId = authMiddleware.authenticateWithRole(
                exchange, UserRole.PATIENT, UserRole.ADMIN);

        Map<String, Object> body =
                JsonUtil.fromJson(exchange, HashMap.class);

        String doctorId       = (String) body.get("doctorId");
        String scheduledAt    = (String) body.get("scheduledAt");
        String notes          = (String) body.getOrDefault(
                "notes", "");
        int    durationMinutes = body.containsKey("durationMinutes")
                ? ((Number) body.get("durationMinutes")).intValue()
                : 30;

        if (doctorId == null || scheduledAt == null) {
            HttpUtil.sendError(exchange, 400,
                    "doctorId and scheduledAt are required");
            return;
        }

        var appointment = appointmentService.book(
                doctorId, patientId, scheduledAt,
                durationMinutes, notes,
                HttpUtil.getClientIp(exchange));

        HttpUtil.sendResponse(exchange, 201,
                JsonUtil.toJson(appointment));
    }


    public void getMyAppointments(HttpExchange exchange)
            throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        UserRole role = authMiddleware.extractRole(exchange);

        var appointments = role == UserRole.DOCTOR
                ? appointmentService.getForDoctor(userId)
                : appointmentService.getForPatient(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("total",        appointments.size());
        response.put("appointments", appointments);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    public void complete(HttpExchange exchange) throws IOException {
        String doctorId = authMiddleware.authenticateWithRole(
                exchange, UserRole.DOCTOR, UserRole.ADMIN);

        String appointmentId = extractId(
                exchange.getRequestURI().getPath());

        var appointment = appointmentService.complete(
                appointmentId, doctorId,
                HttpUtil.getClientIp(exchange));

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(appointment));
    }


    public void cancel(HttpExchange exchange) throws IOException {
        String requesterId = authMiddleware.authenticate(exchange);

        String appointmentId = extractId(
                exchange.getRequestURI().getPath());

        var appointment = appointmentService.cancel(
                appointmentId, requesterId,
                HttpUtil.getClientIp(exchange));

        Map<String, Object> response = new HashMap<>();
        response.put("message",     "Appointment cancelled");
        response.put("appointment", appointment);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }

    public String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}