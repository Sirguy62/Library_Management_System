package com.hospital.cache;

import redis.clients.jedis.Jedis;

public class CacheService {

    public static final int TTL_DOCTOR_PROFILE      = 600;
    public static final int TTL_PATIENT_PROFILE     = 600;
    public static final int TTL_APPOINTMENTS        = 120;
    public static final int TTL_RATE_LIMIT          = 900;
    public static final int TTL_BLACKLISTED_TOKEN   = 900;

    private CacheService() {}



    public static void set(String key, String value, int ttlSeconds) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.setex(key, ttlSeconds, value);
        }
    }


    public static String get(String key) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            return jedis.get(key);
        }
    }



    public static void delete(String key) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.del(key);
        }
    }



    public static boolean exists(String key) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            return jedis.exists(key);
        }
    }



    public static void setForever(String key, String value) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.set(key, value);
        }
    }



    public static long increment(String key) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            return jedis.incr(key);
        }
    }



    public static void expire(String key, int ttlSeconds) {
        try (Jedis jedis = RedisConnection.getPool().getResource()) {
            jedis.expire(key, ttlSeconds);
        }
    }




    public static String keyDoctor(String userId) {
        return "doctor:" + userId;
    }

    public static String keyPatient(String userId) {
        return "patient:" + userId;
    }

    public static String keyAppointments(String doctorId) {
        return "appointments:doctor:" + doctorId;
    }

    public static String keyBlacklist(String jwtId) {
        return "blacklist:" + jwtId;
    }

    public static String keyRateLimit(String email) {
        return "ratelimit:" + email.toLowerCase();
    }
}
