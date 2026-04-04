package com.hospital.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class JsonUtil {

    private JsonUtil() {}

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    public static String toJson(Object obj) throws IOException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromJson(HttpExchange exchange,
                                 Class<T> clazz) throws IOException {
        return MAPPER.readValue(exchange.getRequestBody(), clazz);
    }

    public static <T> T fromJson(String json,
                                 Class<T> clazz) throws IOException {
        return MAPPER.readValue(json, clazz);
    }
}
