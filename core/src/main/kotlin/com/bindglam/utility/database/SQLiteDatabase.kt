package com.bindglam.utility.database

import org.bukkit.configuration.ConfigurationSection
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class SQLiteDatabase : Database {
    private var connection: Connection? = null

    init {
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("Failed to load database driver", e)
        }
    }

    // config is not used
    override fun connect(config: ConfigurationSection?) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/BindglamUtility/database.db")
        } catch (e: SQLException) {
            throw RuntimeException("Failed to load database", e)
        }
    }

    override fun close() {
        try {
            if (connection != null) {
                connection!!.close()
            }
        } catch (e: SQLException) {
            throw RuntimeException("Failed to close database", e)
        } finally {
            connection = null
        }
    }

    override fun getConnection(): Connection {
        try {
            if (connection == null || connection?.isValid(VALID_TIMEOUT) == false) {
                close()
                connect(null)
            }
        } catch (e: SQLException) {
            throw RuntimeException("Failed to ensure connection", e)
        }
        return connection!!
    }

    override fun evictConnection(connection: Connection) {
    }

    companion object {
        private const val VALID_TIMEOUT = 500
    }
}