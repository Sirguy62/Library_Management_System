package com.hospital.util;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class RequestLogger {

    private RequestLogger() {}

    public static long start(HttpExchange exchange) {
        return System.currentTimeMillis();
    }

    public static void finish(HttpExchange exchange,
                              String userId,
                              int statusCode,
                              long startTime) {
        long durationMs = System.currentTimeMillis() - startTime;
        String method   = exchange.getRequestMethod().toUpperCase();
        String path     = exchange.getRequestURI().getPath();
        String ip       = HttpUtil.getClientIp(exchange);

        Logger.request(method, path, userId,
                ip, statusCode, durationMs);
    }

    public static void warnIfSlow(String path,
                                  long durationMs,
                                  long thresholdMs) {
        if (durationMs > thresholdMs) {
            Map<String, Object> ctx = new HashMap<>();
            ctx.put("path",        path);
            ctx.put("durationMs",  durationMs);
            ctx.put("thresholdMs", thresholdMs);
            Logger.warn("SLOW_REQUEST",
                    "Request took " + durationMs + "ms", ctx);
        }
    }
}