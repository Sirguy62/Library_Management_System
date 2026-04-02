package com.authapi;

import com.authapi.engine.RiskCalculator;
import com.authapi.handler.AlertHandler;
import com.authapi.handler.AuthHandler;
import com.authapi.handler.LoanHandler;
import com.authapi.handler.PaymentHandler;
import com.authapi.handler.RiskHandler;
import com.authapi.handler.UserHandler;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.store.LoanStore;
import com.authapi.store.UserStore;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {

        UserStore userStore = new UserStore();
        LoanStore loanStore = new LoanStore();

        AuthMiddleware authMiddleware = new AuthMiddleware(userStore);

        RiskCalculator riskCalculator = new RiskCalculator(loanStore);

        AuthHandler    authHandler    = new AuthHandler(userStore, authMiddleware);
        UserHandler    userHandler    = new UserHandler(userStore, authMiddleware);
        LoanHandler    loanHandler    = new LoanHandler(loanStore, authMiddleware);
        PaymentHandler paymentHandler = new PaymentHandler(loanStore, authMiddleware);
        RiskHandler    riskHandler    = new RiskHandler(authMiddleware, riskCalculator);
        AlertHandler   alertHandler   = new AlertHandler(loanStore, riskCalculator, authMiddleware);

        Router router = new Router(
                authHandler,
                userHandler,
                loanHandler,
                paymentHandler,
                riskHandler,
                alertHandler
        );

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", router);
        server.setExecutor(Executors.newFixedThreadPool(10));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down");
            server.stop(1);
            System.out.println("Server stopped.");
        }));

        try (java.sql.Connection conn = com.authapi.db.DatabaseConnection.getConnection()) {
            System.out.println("Db connected");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            System.exit(1);
        }

        server.start();

        System.out.println("  Credit Risk API running on http://localhost:8080");
        System.out.println("  Auth:");
        System.out.println("    POST   /auth/register");
        System.out.println("    POST   /auth/login");
        System.out.println("  Users:");
        System.out.println("    GET    /users/me");
        System.out.println("    PUT    /users/me");
        System.out.println("    DELETE /users/me");
        System.out.println("  Loans:");
        System.out.println("    POST   /loans");
        System.out.println("    GET    /loans");
        System.out.println("    GET    /loans/{id}");
        System.out.println("    PUT    /loans/{id}");
        System.out.println("    DELETE /loans/{id}");
        System.out.println("  Payments:");
        System.out.println("    POST   /loans/{id}/payments");
        System.out.println("    GET    /loans/{id}/payments");
        System.out.println("  Risk:");
        System.out.println("    GET    /risk/scores");
        System.out.println("    GET    /risk/scores/me");
        System.out.println("    GET    /risk/high");
        System.out.println("  Alerts:");
        System.out.println("    GET    /alerts/overdue");
        System.out.println("    GET    /alerts/high-risk");

    }
}