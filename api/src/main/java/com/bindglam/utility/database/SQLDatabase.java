package com.bindglam.utility.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface SQLDatabase extends Database {
    @NotNull Connection getConnection();

    void evictConnection(@NotNull Connection connection);
}
