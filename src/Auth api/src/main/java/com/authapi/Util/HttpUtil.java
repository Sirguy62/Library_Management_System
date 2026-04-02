package com.authapi.Util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpUtil {

    private HttpUtil() {}

    public static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String body = String.format("{\"error\": \"%s\"}", message);
        sendResponse(exchange, statusCode, body);
    }

    public static String getHeader(HttpExchange exchange, String name) {
        var header = exchange.getRequestHeaders();
        var values = header.get(name);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    public static Map<String, String> parseQueryParams(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || query.isBlank()) return Map.of();

        var params = new java.util.HashMap<String, String>();
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}
