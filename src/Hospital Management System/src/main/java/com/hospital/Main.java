package com.hospital;

import com.hospital.auth.JwtUtil;
import com.hospital.cache.RedisConnection;
import com.hospital.db.DatabaseConnection;
import com.hospital.handler.*;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.service.*;
import com.hospital.store.*;
import com.hospital.util.Logger;
import com.sun.net.httpserver.HttpServer;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {

        JwtUtil.initialize();
        Logger.info("STARTUP", "RSA key pair initialized");

        try (Connection conn = DatabaseConnection.getConnection()) {
            Logger.info("STARTUP", "PostgreSQL connected successfully");
        } catch (Exception e) {
            Logger.fatal("STARTUP_FAILED",
                    "PostgreSQL connection failed", e);
            System.exit(1);
        }

        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.ping();
            Logger.info("STARTUP", "Redis connected successfully");
        } catch (Exception e) {
            Logger.fatal("STARTUP_FAILED",
                    "Redis connection failed", e);
            System.exit(1);
        }

        UserStore         userStore         = new UserStore();
        TokenStore        tokenStore        = new TokenStore();
        VerificationStore verificationStore = new VerificationStore();
        AuditStore        auditStore        = new AuditStore();
        AppointmentStore  appointmentStore  = new AppointmentStore();
        BillStore         billStore         = new BillStore();

        AuthMiddleware authMiddleware = new AuthMiddleware(tokenStore);

        AuthService authService = new AuthService(
                userStore, tokenStore,
                verificationStore, auditStore);

        AppointmentService appointmentService = new AppointmentService(
                appointmentStore, userStore, auditStore);

        BillingService billingService = new BillingService(
                billStore, appointmentStore, auditStore);

        AuthHandler authHandler = new AuthHandler(
                authService, authMiddleware);

        DoctorHandler doctorHandler = new DoctorHandler(
                userStore, appointmentStore, authMiddleware);

        PatientHandler patientHandler = new PatientHandler(
                userStore, appointmentStore,
                billingService, authMiddleware);

        AppointmentHandler appointmentHandler =
                new AppointmentHandler(
                        appointmentService, authMiddleware);

        BillingHandler billingHandler = new BillingHandler(
                billingService, authMiddleware);

        AdminHandler adminHandler = new AdminHandler(
                userStore, auditStore, authMiddleware);

        Router router = new Router(
                authHandler,
                doctorHandler,
                patientHandler,
                appointmentHandler,
                billingHandler,
                adminHandler
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0);
        server.createContext("/", router);
        server.setExecutor(Executors.newFixedThreadPool(10));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down...");
            server.stop(2);
            RedisConnection.getPool().close();
            System.out.println("Server stopped cleanly.");
        }));

        server.start();
        printRoutes();
    }

    private static void printRoutes() {
        System.out.println("Hospital Management API — Running");
        System.out.println("http://localhost:8080");
        System.out.println("AUTH (public)");
        System.out.println("POST   /auth/register");
        System.out.println("GET    /auth/verify?token=xxx ");
        System.out.println("POST   /auth/login");
        System.out.println("POST   /auth/refresh");
        System.out.println("POST   /auth/logout");
        System.out.println("POST   /auth/logout/all");
        System.out.println("DOCTORS");
        System.out.println("GET    /doctors");
        System.out.println("GET    /doctors/{id}");
        System.out.println("GET    /doctors/{id}/appointments");
        System.out.println("GET    /patients/{id} ");
        System.out.println(" GET    /patients/{id}/appointments");
        System.out.println(" GET    /patients/{id}/bills       ");
        System.out.println("  APPOINTMENTS           ");
        System.out.println(" POST   /appointments ");
        System.out.println(" GET    /appointments ");
        System.out.println(" PUT    /appointments/{id}/complete");
        System.out.println(" DELETE /appointments/{id}         ");
        System.out.println("  BILLING   ");
        System.out.println(" POST   /bills        ");
        System.out.println(" GET    /bills/{id}   ");
        System.out.println(" PUT    /bills/{id}/pay            ");
        System.out.println("  ADMIN     ");
        System.out.println(" GET    /admin/users               ");
        System.out.println(" GET    /admin/audit               ");
        System.out.println(" GET    /admin/stats               ");
        System.out.println("  Press Ctrl+C to stop");
        System.out.println();
    }
}