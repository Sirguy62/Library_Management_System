package com.gateway.controller;

import com.gateway.dto.PaymentRequest;
import com.gateway.dto.PaymentResponse;
import com.gateway.model.Transaction;
import com.gateway.service.PaymentService;
import com.gateway.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PaymentController implements HttpHandler {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        PaymentRequest request =
                JsonUtil.read(exchange.getRequestBody(), PaymentRequest.class);

        Transaction tx = service.processPayment(
                request.accountId,
                request.amount,
                request.idempotencyKey
        );

        PaymentResponse response =
                new PaymentResponse(tx.getId(), tx.getStatus());

        byte[] json = JsonUtil.write(response);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.length);
        exchange.getResponseBody().write(json);
        exchange.close();
    }
}