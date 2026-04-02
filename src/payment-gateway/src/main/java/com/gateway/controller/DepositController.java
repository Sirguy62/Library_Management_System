package com.gateway.controller;

import com.gateway.dto.DepositRequest;
import com.gateway.dto.DepositResponse;
import com.gateway.model.Transaction;
import com.gateway.service.PaymentService;
import com.gateway.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class DepositController implements HttpHandler {

    private final PaymentService service;

    public DepositController(PaymentService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        DepositRequest request =
                JsonUtil.read(exchange.getRequestBody(), DepositRequest.class);

        Transaction tx =
                service.deposit(request.accountId, request.amount);

        double balance =
                service.getAccountBalance(request.accountId);

        DepositResponse response =
                new DepositResponse(
                        tx.getId(),
                        tx.getStatus(),
                        balance
                );

        byte[] json = JsonUtil.write(response);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.length);
        exchange.getResponseBody().write(json);
        exchange.close();
    }
}