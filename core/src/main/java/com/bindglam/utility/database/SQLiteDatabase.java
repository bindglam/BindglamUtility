package com.bindglam.utility.database;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase implements Database {
    private static final int VALID_TIMEOUT = 500;

    private Connection connection;

    @Override
    public void connect(ConfigurationSection config) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver", e);
        }

        try {
            createConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load database", e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database", e);
        } finally {
            connection = null;
        }
    }

    @Override
    public @NotNull Connection getConnection() {
        try {
            if(connection == null || connection.isValid(VALID_TIMEOUT)) {
                close();
                createConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to ensure connection", e);
        }
        return connection;
    }

    @Override
    public void evictConnection(@NotNull Connection connection) {
    }

    private void createConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:plugins/BindglamUtility/database.db");
    }
}
