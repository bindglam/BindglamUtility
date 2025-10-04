package com.bindglam.utility.database;

import redis.clients.jedis.Jedis;

import java.io.Closeable;

public final class JedisWrapper implements Closeable {
    private final Jedis jedis;

    public JedisWrapper(Jedis jedis) {
        this.jedis = jedis;
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public void expire(String key, int seconds) {
        jedis.expire(key, seconds);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
