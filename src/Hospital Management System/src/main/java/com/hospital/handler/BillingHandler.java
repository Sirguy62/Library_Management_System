package com.hospital.handler;

import com.hospital.enums.UserRole;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.service.BillingService;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BillingHandler {

    private final BillingService billingService;
    private final AuthMiddleware authMiddleware;

    public BillingHandler(BillingService billingService,
                          AuthMiddleware authMiddleware) {
        this.billingService = billingService;
        this.authMiddleware = authMiddleware;
    }


    public void generate(HttpExchange exchange) throws IOException {
        String requesterId = authMiddleware.authenticateWithRole(
                exchange, UserRole.DOCTOR, UserRole.ADMIN);

        Map<String, Object> body =
                JsonUtil.fromJson(exchange, HashMap.class);

        String appointmentId   = (String) body.get("appointmentId");
        double discountPercent = body.containsKey("discount")
                ? ((Number) body.get("discount")).doubleValue()
                : 0.0;

        if (appointmentId == null) {
            HttpUtil.sendError(exchange, 400,
                    "appointmentId is required");
            return;
        }

        var bill = billingService.generate(
                appointmentId, discountPercent,
                requesterId,
                HttpUtil.getClientIp(exchange));

        HttpUtil.sendResponse(exchange, 201,
                JsonUtil.toJson(bill));
    }


    public void getOne(HttpExchange exchange) throws IOException {
        authMiddleware.authenticate(exchange);

        String billId = extractId(
                exchange.getRequestURI().getPath());

        var bill = billingService.getBill(billId);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(bill));
    }


    public void markPaid(HttpExchange exchange) throws IOException {
        String requesterId = authMiddleware.authenticateWithRole(
                exchange, UserRole.ADMIN);

        String billId = extractId(
                exchange.getRequestURI().getPath());

        var bill = billingService.markPaid(
                billId, requesterId,
                HttpUtil.getClientIp(exchange));

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bill marked as paid");
        response.put("bill",    bill);

        HttpUtil.sendResponse(exchange, 200,
                JsonUtil.toJson(response));
    }

    public String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}