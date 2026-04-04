package com.hospital.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

    private static final JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);        // max 10 connections at once
        config.setMaxIdle(5);          // keep 5 idle connections ready
        config.setMinIdle(2);          // always keep at least 2 open
        pool = new JedisPool(config, "localhost", 6379);
    }

    private RedisConnection() {}

    public static JedisPool getPool() {
        return pool;
    }
}