package com.inventoryapi;

import com.inventoryapi.engine.StockAlertEngine;
import com.inventoryapi.handler.*;
import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.store.*;
import com.inventoryapi.db.DatabaseConnection;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("  Database connected successfully");
        } catch (Exception e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            System.exit(1);
        }

        UserStore     userStore     = new UserStore();
        CategoryStore categoryStore = new CategoryStore();
        SupplierStore supplierStore = new SupplierStore();
        ProductStore  productStore  = new ProductStore();
        StockStore    stockStore    = new StockStore();

        AuthMiddleware authMiddleware = new AuthMiddleware(userStore);

        StockAlertEngine alertEngine = new StockAlertEngine(productStore, stockStore);

        AuthHandler     authHandler     = new AuthHandler(userStore, authMiddleware);
        CategoryHandler categoryHandler = new CategoryHandler(categoryStore, authMiddleware);
        SupplierHandler supplierHandler = new SupplierHandler(supplierStore, authMiddleware);
        ProductHandler  productHandler  = new ProductHandler(productStore, categoryStore,
                supplierStore, authMiddleware);
        StockHandler    stockHandler    = new StockHandler(productStore, stockStore, authMiddleware);

        Router router = new Router(
                authHandler,
                categoryHandler,
                supplierHandler,
                productHandler,
                stockHandler,
                alertEngine
        );

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", router);
        server.setExecutor(Executors.newFixedThreadPool(10));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            server.stop(1);
            System.out.println("Server stopped.");
        }));

        server.start();

        System.out.println("  Inventory API running on http://localhost:8080");
        System.out.println("  Auth:");
        System.out.println("    POST   /auth/register");
        System.out.println("    POST   /auth/login");
        System.out.println("  Categories:");
        System.out.println("    POST   /categories");
        System.out.println("    GET    /categories");
        System.out.println("    GET    /categories/{id}");
        System.out.println("    PUT    /categories/{id}");
        System.out.println("    DELETE /categories/{id}");
        System.out.println("  Suppliers:");
        System.out.println("    POST   /suppliers");
        System.out.println("    GET    /suppliers");
        System.out.println("    GET    /suppliers/{id}");
        System.out.println("    PUT    /suppliers/{id}");
        System.out.println("    DELETE /suppliers/{id}");
        System.out.println("  Products:");
        System.out.println("    POST   /products");
        System.out.println("    GET    /products");
        System.out.println("    GET    /products/{id}");
        System.out.println("    PUT    /products/{id}");
        System.out.println("    DELETE /products/{id}");
        System.out.println("    GET    /products/low-stock");
        System.out.println("  Stock:");
        System.out.println("    POST   /products/{id}/restock");
        System.out.println("    POST   /products/{id}/sell");
        System.out.println("    GET    /products/{id}/movements");
        System.out.println("  Alerts:");
        System.out.println("    GET    /alerts/low-stock");
        System.out.println("    GET    /alerts/out-of-stock");
        System.out.println("    GET    /alerts/summary");

    }
}
