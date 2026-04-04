package com.hospital.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpUtil {

    private HttpUtil() {}

    public static void sendResponse(HttpExchange exchange,
                                    int statusCode,
                                    String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders()
                .set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendError(HttpExchange exchange,
                                 int statusCode,
                                 String message) throws IOException {
        String body = String.format(
                "{\"error\": \"ERROR\", \"message\": \"%s\", \"statusCode\": %d}",
                message, statusCode);
        sendResponse(exchange, statusCode, body);
    }

    public static String getHeader(HttpExchange exchange, String name) {
        var headers = exchange.getRequestHeaders();
        var values  = headers.get(name);
        if (values == null) {
            for (var entry : headers.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    values = entry.getValue();
                    break;
                }
            }
        }
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    public static String extractBearerToken(HttpExchange exchange) {
        String header = getHeader(exchange, "Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return header.substring(7);
    }

    public static Map<String, String> parseQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || query.isBlank()) return Map.of();

        var params = new java.util.HashMap<String, String>();
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) params.put(kv[0], kv[1]);
        }
        return params;
    }

    public static String getClientIp(HttpExchange exchange) {
        String forwarded = getHeader(exchange, "X-Forwarded-For");
        if (forwarded != null) return forwarded.split(",")[0].trim();
        return exchange.getRemoteAddress().getAddress().getHostAddress();
    }
}