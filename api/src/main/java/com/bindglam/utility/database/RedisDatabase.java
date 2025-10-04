package com.bindglam.utility.database;

import org.jetbrains.annotations.NotNull;

public interface RedisDatabase extends Database {
    @NotNull JedisWrapper getResource();
}
