package com.hospital.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    public enum Level {
        DEBUG, INFO, WARN, ERROR, FATAL
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String       LOG_FILE = "hospital-api.log";
    private static       Level        MIN_LEVEL = Level.INFO;

    private Logger() {}


    public static void log(Level level,
                           String event,
                           String message,
                           Map<String, Object> context) {
        if (level.ordinal() < MIN_LEVEL.ordinal()) return;

        Map<String, Object> entry = new HashMap<>();
        entry.put("timestamp", Instant.now().toString());
        entry.put("level",     level.name());
        entry.put("event",     event);
        entry.put("message",   message);

        if (context != null) {
            entry.putAll(context);
        }

        String json = toJson(entry);

        printColored(level, json);

        writeToFile(json);
    }


    public static void info(String event, String message) {
        log(Level.INFO, event, message, null);
    }

    public static void info(String event,
                            String message,
                            Map<String, Object> context) {
        log(Level.INFO, event, message, context);
    }

    public static void warn(String event, String message) {
        log(Level.WARN, event, message, null);
    }

    public static void warn(String event,
                            String message,
                            Map<String, Object> context) {
        log(Level.WARN, event, message, context);
    }

    public static void error(String event,
                             String message,
                             Throwable throwable) {
        Map<String, Object> context = new HashMap<>();
        context.put("errorClass",   throwable.getClass().getSimpleName());
        context.put("errorMessage", throwable.getMessage());
        context.put("stackTrace",   getStackTrace(throwable));
        log(Level.ERROR, event, message, context);
    }

    public static void error(String event,
                             String message,
                             Throwable throwable,
                             Map<String, Object> context) {
        context.put("errorClass",   throwable.getClass().getSimpleName());
        context.put("errorMessage", throwable.getMessage());
        context.put("stackTrace",   getStackTrace(throwable));
        log(Level.ERROR, event, message, context);
    }

    public static void fatal(String event,
                             String message,
                             Throwable throwable) {
        Map<String, Object> context = new HashMap<>();
        context.put("errorClass",   throwable.getClass().getSimpleName());
        context.put("errorMessage", throwable.getMessage());
        context.put("stackTrace",   getStackTrace(throwable));
        log(Level.FATAL, event, message, context);
    }


    public static void request(String method,
                               String path,
                               String userId,
                               String ipAddress,
                               int statusCode,
                               long durationMs) {
        Map<String, Object> context = new HashMap<>();
        context.put("method",     method);
        context.put("path",       path);
        context.put("userId",     userId);
        context.put("ipAddress",  ipAddress);
        context.put("statusCode", statusCode);
        context.put("durationMs", durationMs);

        Level level = statusCode >= 500 ? Level.ERROR
                : statusCode >= 400 ? Level.WARN
                : Level.INFO;

        log(level, "HTTP_REQUEST",
                method + " " + path + " → " + statusCode,
                context);
    }


    public static void business(String event,
                                String userId,
                                String description,
                                Map<String, Object> context) {
        if (context == null) context = new HashMap<>();
        context.put("userId", userId);
        log(Level.INFO, event, description, context);
    }


    private static String toJson(Map<String, Object> entry) {
        try {
            return MAPPER.writeValueAsString(entry);
        } catch (Exception e) {
            return "{\"error\":\"Failed to serialize log entry\"}";
        }
    }

    private static void writeToFile(String json) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(json);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: "
                    + e.getMessage());
        }
    }

    private static void printColored(Level level, String json) {
        String color = switch (level) {
            case DEBUG -> "\u001B[37m";
            case INFO  -> "\u001B[32m";
            case WARN  -> "\u001B[33m";
            case ERROR -> "\u001B[31m";
            case FATAL -> "\u001B[35m";
        };
        String reset = "\u001B[0m";
        System.out.println(color + json + reset);
    }

    private static String getStackTrace(Throwable t) {
        java.io.StringWriter sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        String trace = sw.toString();
        return trace.length() > 500
                ? trace.substring(0, 500) + "..."
                : trace;
    }
}