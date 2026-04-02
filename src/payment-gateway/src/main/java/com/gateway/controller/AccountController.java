package com.gateway.controller;

import com.gateway.dto.CreateAccountRequest;
import com.gateway.dto.CreateAccountResponse;
import com.gateway.model.Account;
import com.gateway.service.AccountService;
import com.gateway.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AccountController implements HttpHandler {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            // If client sends GET or any other reguest that isn't POST → reject
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // Read Request Body (JSON → DTO)
            CreateAccountRequest request =
                    JsonUtil.read(exchange.getRequestBody(),
                            CreateAccountRequest.class);

            Account account =
                    service.createAccount(request.name);

            CreateAccountResponse response =
                    new CreateAccountResponse(
                            account.getId(),
                            account.getName(),
                            account.getBalance()
                    );

            byte[] json = JsonUtil.write(response);

            exchange.getResponseHeaders()
                    .add("Content-Type", "application/json");

            exchange.sendResponseHeaders(200, json.length);

            exchange.getResponseBody().write(json);

            exchange.close();

        } catch (Exception e) {

            e.printStackTrace();

            String msg = e.getMessage();

            exchange.sendResponseHeaders(500, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        }
    }
}