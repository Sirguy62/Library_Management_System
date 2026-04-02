package com.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T read(InputStream is, Class<T> clazz) throws IOException {
        return mapper.readValue(is, clazz);
    }

    public static byte[] write(Object obj) throws IOException {
        return mapper.writeValueAsBytes(obj);
    }
}