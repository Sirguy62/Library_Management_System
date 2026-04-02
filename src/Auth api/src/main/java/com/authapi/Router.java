package com.authapi;

import com.authapi.Util.HttpUtil;
import com.authapi.handler.AlertHandler;
import com.authapi.handler.AuthHandler;
import com.authapi.handler.LoanHandler;
import com.authapi.handler.PaymentHandler;
import com.authapi.handler.RiskHandler;
import com.authapi.handler.UserHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Router implements HttpHandler {

    private final AuthHandler    authHandler;
    private final UserHandler    userHandler;
    private final LoanHandler    loanHandler;
    private final PaymentHandler paymentHandler;
    private final RiskHandler    riskHandler;
    private final AlertHandler   alertHandler;

    public Router(
            AuthHandler    authHandler,
            UserHandler    userHandler,
            LoanHandler    loanHandler,
            PaymentHandler paymentHandler,
            RiskHandler    riskHandler,
            AlertHandler   alertHandler
    ) {
        this.authHandler    = authHandler;
        this.userHandler    = userHandler;
        this.loanHandler    = loanHandler;
        this.paymentHandler = paymentHandler;
        this.riskHandler    = riskHandler;
        this.alertHandler   = alertHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path   = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod().toUpperCase();

        System.out.println("[" + method + "] " + path);

        try {

            if (path.equals("/auth/register") && method.equals("POST")) {
                authHandler.register(exchange);

            } else if (path.equals("/auth/login") && method.equals("POST")) {
                authHandler.login(exchange);

            } else if (path.equals("/users/me") && method.equals("GET")) {
                userHandler.getUser(exchange);

            } else if (path.equals("/users/me") && method.equals("PUT")) {
                userHandler.updateUser(exchange);

            } else if (path.equals("/users/me") && method.equals("DELETE")) {
                userHandler.deleteUser(exchange);

            } else if (path.equals("/loans") && method.equals("POST")) {
                loanHandler.createLoan(exchange);

            } else if (path.equals("/loans") && method.equals("GET")) {
                loanHandler.getLoans(exchange);

            } else if (path.matches("/loans/[^/]+") && method.equals("GET")) {
                loanHandler.getLoan(exchange);

            } else if (path.matches("/loans/[^/]+") && method.equals("PUT")) {
                loanHandler.updateLoan(exchange);

            } else if (path.matches("/loans/[^/]+") && method.equals("DELETE")) {
                loanHandler.deleteLoan(exchange);

            } else if (path.matches("/loans/[^/]+/payments") && method.equals("POST")) {
                paymentHandler.recordPayment(exchange);

            } else if (path.matches("/loans/[^/]+/payments") && method.equals("GET")) {
                paymentHandler.getPayment(exchange);

            } else if (path.equals("/risk/scores") && method.equals("GET")) {
                riskHandler.getRiskScore(exchange);

            } else if (path.equals("/risk/high") && method.equals("GET")) {
                riskHandler.getHighRiskLoans(exchange);

            } else if (path.equals("/risk/scores/me") && method.equals("GET")) {
                riskHandler.getMyRiskScore(exchange);

            } else if (path.equals("/alerts/overdue") && method.equals("GET")) {
                alertHandler.getOverdueAlerts(exchange);

            } else if (path.equals("/alerts/high-risk") && method.equals("GET")) {
                alertHandler.getHighRiskAlerts(exchange);

            } else {
                HttpUtil.sendError(exchange, 404, "Route not found");
            }

        } catch (Exception e) {
            System.err.println("Unhandled error: " + e.getMessage());
            e.printStackTrace();
            HttpUtil.sendError(exchange, 500, "Internal server error");
        }
    }
}