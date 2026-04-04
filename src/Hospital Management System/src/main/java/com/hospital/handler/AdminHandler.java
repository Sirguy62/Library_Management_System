package com.hospital.handler;

import com.hospital.enums.AuditEventType;
import com.hospital.enums.UserRole;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.store.AuditStore;
import com.hospital.store.UserStore;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminHandler {

    private final UserStore      userStore;
    private final AuditStore     auditStore;
    private final AuthMiddleware authMiddleware;

    public AdminHandler(UserStore userStore,
                        AuditStore auditStore,
                        AuthMiddleware authMiddleware) {
        this.userStore      = userStore;
        this.auditStore     = auditStore;
        this.authMiddleware = authMiddleware;
    }


    public void getAllUsers(HttpExchange exchange) throws IOException {
        authMiddleware.authenticateWithRole(
                exchange, UserRole.ADMIN);

        Map<String, Object> response = new HashMap<>();
        response.put("doctors",  userStore
                .findAllByRole(UserRole.DOCTOR).size());
        response.put("patients", userStore
                .findAllByRole(UserRole.PATIENT).size());
        response.put("nurses",   userStore
                .findAllByRole(UserRole.NURSE).size());

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    public void getAuditLog(HttpExchange exchange) throws IOException {
        authMiddleware.authenticateWithRole(
                exchange, UserRole.ADMIN);

        Map<String, String> params =
                HttpUtil.parseQueryParams(exchange);
        String eventTypeStr = params.get("eventType");

        var logs = eventTypeStr != null
                ? auditStore.findByEventType(
                AuditEventType.fromString(eventTypeStr))
                : auditStore.findByEventType(
                AuditEventType.LOGIN_SUCCESS);

        Map<String, Object> response = new HashMap<>();
        response.put("total", logs.size());
        response.put("logs",  logs);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }


    public void getStats(HttpExchange exchange) throws IOException {
        authMiddleware.authenticateWithRole(
                exchange, UserRole.ADMIN);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDoctors",  userStore
                .findAllByRole(UserRole.DOCTOR).size());
        stats.put("totalPatients", userStore
                .findAllByRole(UserRole.PATIENT).size());
        stats.put("totalNurses",   userStore
                .findAllByRole(UserRole.NURSE).size());
        stats.put("recentLogins",  auditStore
                .findByEventType(AuditEventType.LOGIN_SUCCESS)
                .size());
        stats.put("failedLogins",  auditStore
                .findByEventType(AuditEventType.LOGIN_FAILED)
                .size());

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(stats));
    }
}
