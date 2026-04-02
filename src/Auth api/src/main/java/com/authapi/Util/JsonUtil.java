package com.authapi.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class JsonUtil {

    private JsonUtil() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson (HttpExchange exchange, Class<T> classt) throws IOException {
        return objectMapper.readValue(exchange.getRequestBody(), classt);
    }

    public static <T> T fromJson (String json, Class<T> classtt) throws IOException {
        return objectMapper.readValue(json, classtt);
    }
}
