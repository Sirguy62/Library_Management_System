package com.hospital;

import com.hospital.exception.AppException;
import com.hospital.handler.*;
import com.hospital.util.HttpUtil;
import com.hospital.util.Logger;
import com.hospital.util.RequestLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Router implements HttpHandler {

    private final AuthHandler        authHandler;
    private final DoctorHandler      doctorHandler;
    private final PatientHandler     patientHandler;
    private final AppointmentHandler appointmentHandler;
    private final BillingHandler     billingHandler;
    private final AdminHandler       adminHandler;

    public Router(
            AuthHandler        authHandler,
            DoctorHandler      doctorHandler,
            PatientHandler     patientHandler,
            AppointmentHandler appointmentHandler,
            BillingHandler     billingHandler,
            AdminHandler       adminHandler
    ) {
        this.authHandler        = authHandler;
        this.doctorHandler      = doctorHandler;
        this.patientHandler     = patientHandler;
        this.appointmentHandler = appointmentHandler;
        this.billingHandler     = billingHandler;
        this.adminHandler       = adminHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long   startTime = RequestLogger.start(exchange);
        String path      = exchange.getRequestURI().getPath();
        String method    = exchange.getRequestMethod().toUpperCase();
        String userId    = null;
        int    status    = 200;

        try {
            route(exchange, path, method);

        } catch (AppException e) {
            status = e.getStatusCode();
            HttpUtil.sendResponse(exchange,
                    e.getStatusCode(), e.toJson());

            if (e.getStatusCode() >= 500) {
                Logger.error("APP_EXCEPTION",
                        e.getMessage(), e, buildContext(exchange));
            } else {
                Map<String, Object> ctx = buildContext(exchange);
                ctx.put("errorCode", e.getErrorCode());
                Logger.warn("APP_EXCEPTION", e.getMessage(), ctx);
            }

        } catch (Exception e) {
            status = 500;
            Logger.error("UNHANDLED_ERROR",
                    "Unhandled exception in router", e,
                    buildContext(exchange));
            HttpUtil.sendError(exchange, 500,
                    "Internal server error");

        } finally {
            RequestLogger.finish(exchange, userId, status, startTime);
            RequestLogger.warnIfSlow(
                    exchange.getRequestURI().getPath(),
                    System.currentTimeMillis() - startTime,
                    500 // warn if over 500ms
            );
        }
    }

    private void route(HttpExchange exchange,
                       String path,
                       String method) throws IOException {
        if (path.equals("/auth/register") && method.equals("POST")) {
            authHandler.register(exchange);

        } else if (path.equals("/auth/verify") && method.equals("GET")) {
            authHandler.verifyEmail(exchange);

        } else if (path.equals("/auth/login") && method.equals("POST")) {
            authHandler.login(exchange);

        } else if (path.equals("/auth/refresh") && method.equals("POST")) {
            authHandler.refresh(exchange);

        } else if (path.equals("/auth/logout") && method.equals("POST")) {
            authHandler.logout(exchange);

        } else if (path.equals("/auth/logout/all") && method.equals("POST")) {
            authHandler.logoutAll(exchange);

        } else if (path.equals("/doctors") && method.equals("GET")) {
            doctorHandler.getAll(exchange);

        } else if (path.matches("/doctors/[^/]+")
                && method.equals("GET")
                && !path.endsWith("/appointments")) {
            doctorHandler.getOne(exchange);

        } else if (path.matches("/doctors/[^/]+/appointments")
                && method.equals("GET")) {
            doctorHandler.getAppointments(exchange);

        } else if (path.matches("/patients/[^/]+")
                && method.equals("GET")
                && !path.contains("/appointments")
                && !path.contains("/bills")) {
            patientHandler.getOne(exchange);

        } else if (path.matches("/patients/[^/]+/appointments")
                && method.equals("GET")) {
            patientHandler.getAppointments(exchange);

        } else if (path.matches("/patients/[^/]+/bills")
                && method.equals("GET")) {
            patientHandler.getBills(exchange);

        } else if (path.equals("/appointments")
                && method.equals("POST")) {
            appointmentHandler.book(exchange);

        } else if (path.equals("/appointments")
                && method.equals("GET")) {
            appointmentHandler.getMyAppointments(exchange);

        } else if (path.matches("/appointments/[^/]+/complete")
                && method.equals("PUT")) {
            appointmentHandler.complete(exchange);

        } else if (path.matches("/appointments/[^/]+")
                && method.equals("DELETE")) {
            appointmentHandler.cancel(exchange);

        } else if (path.equals("/bills")
                && method.equals("POST")) {
            billingHandler.generate(exchange);

        } else if (path.matches("/bills/[^/]+/pay")
                && method.equals("PUT")) {
            billingHandler.markPaid(exchange);

        } else if (path.matches("/bills/[^/]+")
                && method.equals("GET")) {
            billingHandler.getOne(exchange);

        } else if (path.equals("/admin/users")
                && method.equals("GET")) {
            adminHandler.getAllUsers(exchange);

        } else if (path.equals("/admin/audit")
                && method.equals("GET")) {
            adminHandler.getAuditLog(exchange);

        } else if (path.equals("/admin/stats")
                && method.equals("GET")) {
            adminHandler.getStats(exchange);

        } else {
            HttpUtil.sendError(exchange, 404, "Route not found");
        }
    }

    private Map<String, Object> buildContext(HttpExchange exchange) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("method", exchange.getRequestMethod());
        ctx.put("path",   exchange.getRequestURI().getPath());
        ctx.put("ip",     HttpUtil.getClientIp(exchange));
        return ctx;
    }
}