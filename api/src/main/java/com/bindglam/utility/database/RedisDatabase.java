package com.bindglam.utility.database;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

public interface RedisDatabase extends Database {
    @NotNull Jedis getResource();
}
