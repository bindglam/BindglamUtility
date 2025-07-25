package com.bindglam.utility.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabase implements Database {
    private HikariDataSource hikariDS;

    @Override
    public void connect(ConfigurationSection config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("host") + ":" + config.getString("port") + "/" + config.getString("database") + "?useUnicode=true&characterEncoding=utf8");
        hikariConfig.setUsername(config.getString("user"));
        hikariConfig.setPassword(config.getString("password"));
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(10000);
        hikariConfig.setMinimumIdle(50); // 100
        hikariConfig.setMaximumPoolSize(1000); // 1000

        hikariDS = new HikariDataSource(hikariConfig);
    }

    @Override
    public void close() {
        if(hikariDS == null)
            return;

        if(!hikariDS.isClosed())
            hikariDS.close();
    }

    @Override
    public @NotNull Connection getConnection() {
        try {
            return hikariDS.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void evictConnection(@NotNull Connection connection) {
        hikariDS.evictConnection(connection);
    }
}
