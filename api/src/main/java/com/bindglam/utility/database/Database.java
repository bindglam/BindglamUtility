package com.bindglam.utility.database;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public interface Database {
    void connect(@Nullable ConfigurationSection config);

    void close();
}
