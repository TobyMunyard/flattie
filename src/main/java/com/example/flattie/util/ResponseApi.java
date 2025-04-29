package com.example.flattie.util;

import java.util.Map;

public class ResponseApi {
    public static Map<String, Object> success(String message, Map<String, Object> data) {
        return Map.of("status", "success", "message", message, "data", data);
    }

    public static Map<String, Object> success(String message) {
        return Map.of("status", "success", "message", message);
    }

    public static Map<String, Object> error(String message) {
        return Map.of("status", "error", "message", message);
    }
}