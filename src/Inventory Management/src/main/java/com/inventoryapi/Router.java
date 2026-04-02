package com.inventoryapi;

import com.inventoryapi.engine.StockAlertEngine;
import com.inventoryapi.handler.*;
import com.inventoryapi.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Router implements HttpHandler {

    private final AuthHandler     authHandler;
    private final CategoryHandler categoryHandler;
    private final SupplierHandler supplierHandler;
    private final ProductHandler  productHandler;
    private final StockHandler    stockHandler;
    private final StockAlertEngine alertEngine;

    public Router(
            AuthHandler     authHandler,
            CategoryHandler categoryHandler,
            SupplierHandler supplierHandler,
            ProductHandler  productHandler,
            StockHandler    stockHandler,
            StockAlertEngine alertEngine
    ) {
        this.authHandler     = authHandler;
        this.categoryHandler = categoryHandler;
        this.supplierHandler = supplierHandler;
        this.productHandler  = productHandler;
        this.stockHandler    = stockHandler;
        this.alertEngine     = alertEngine;
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

                // ── Categories ───────────────────────────────────
            } else if (path.equals("/categories") && method.equals("POST")) {
                categoryHandler.create(exchange);

            } else if (path.equals("/categories") && method.equals("GET")) {
                categoryHandler.getAll(exchange);

            } else if (path.matches("/categories/[^/]+") && method.equals("GET")) {
                categoryHandler.getOne(exchange);

            } else if (path.matches("/categories/[^/]+") && method.equals("PUT")) {
                categoryHandler.update(exchange);

            } else if (path.matches("/categories/[^/]+") && method.equals("DELETE")) {
                categoryHandler.delete(exchange);

            } else if (path.equals("/suppliers") && method.equals("POST")) {
                supplierHandler.create(exchange);

            } else if (path.equals("/suppliers") && method.equals("GET")) {
                supplierHandler.getAll(exchange);

            } else if (path.matches("/suppliers/[^/]+") && method.equals("GET")) {
                supplierHandler.getOne(exchange);

            } else if (path.matches("/suppliers/[^/]+") && method.equals("PUT")) {
                supplierHandler.update(exchange);

            } else if (path.matches("/suppliers/[^/]+") && method.equals("DELETE")) {
                supplierHandler.delete(exchange);

                // ── Products — specific routes first ─────────────
            } else if (path.equals("/products/low-stock") && method.equals("GET")) {
                productHandler.getLowStock(exchange);

            } else if (path.equals("/products") && method.equals("POST")) {
                productHandler.create(exchange);

            } else if (path.equals("/products") && method.equals("GET")) {
                productHandler.getAll(exchange);

            } else if (path.matches("/products/[^/]+") && method.equals("GET")) {
                productHandler.getOne(exchange);

            } else if (path.matches("/products/[^/]+") && method.equals("PUT")) {
                productHandler.update(exchange);

            } else if (path.matches("/products/[^/]+") && method.equals("DELETE")) {
                productHandler.delete(exchange);

            } else if (path.matches("/products/[^/]+/restock") && method.equals("POST")) {
                stockHandler.restock(exchange);

            } else if (path.matches("/products/[^/]+/sell") && method.equals("POST")) {
                stockHandler.sell(exchange);

            } else if (path.matches("/products/[^/]+/movements") && method.equals("GET")) {
                stockHandler.getMovements(exchange);

            } else if (path.equals("/alerts/low-stock") && method.equals("GET")) {
                sendAlerts(exchange, alertEngine.getLowStockAlerts(), "lowStockAlerts");

            } else if (path.equals("/alerts/out-of-stock") && method.equals("GET")) {
                sendAlerts(exchange, alertEngine.getOutOfStockAlerts(), "outOfStockAlerts");

            } else if (path.equals("/alerts/summary") && method.equals("GET")) {
                HttpUtil.sendResponse(exchange, 200,
                        com.inventoryapi.util.JsonUtil.toJson(alertEngine.getFullSummary()));

            } else {
                HttpUtil.sendError(exchange, 404, "Route not found");
            }

        } catch (Exception e) {
            System.err.println("Unhandled error: " + e.getMessage());
            e.printStackTrace();
            HttpUtil.sendError(exchange, 500, "Internal server error");
        }
    }


    private void sendAlerts(HttpExchange exchange, java.util.List<?> alerts,
                            String key) throws IOException {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("total", alerts.size());
        response.put(key,     alerts);
        HttpUtil.sendResponse(exchange, 200,
                com.inventoryapi.util.JsonUtil.toJson(response));
    }
}