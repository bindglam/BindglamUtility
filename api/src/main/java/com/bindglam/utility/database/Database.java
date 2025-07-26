package com.bindglam.utility.database;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

public interface Database {
    void connect(@Nullable ConfigurationSection config);

    void close();

    @NotNull Connection getConnection();

    void evictConnection(@NotNull Connection connection);
}
