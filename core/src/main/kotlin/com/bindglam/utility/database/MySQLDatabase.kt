package com.bindglam.utility.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.configuration.ConfigurationSection
import java.sql.Connection
import java.sql.SQLException

class MySQLDatabase : SQLDatabase {
    private var hikariDS: HikariDataSource? = null

    override fun connect(config: ConfigurationSection?) {
        requireNotNull(config) { "MySQL config 설정이 누락되었습니다." }

        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
        hikariConfig.jdbcUrl = "jdbc:mysql://" + config.getString("host") + ":" + config.getString("port") + "/" + config.getString("database") + "?useUnicode=true&characterEncoding=utf8"
        hikariConfig.username = config.getString("user")
        hikariConfig.password = config.getString("password")

        val poolConfig = config.getConfigurationSection("pool")
        val minimumIdle = poolConfig?.getInt("minimumIdle", 10) ?: 10
        val configuredMaximumPoolSize = poolConfig?.getInt("maximumPoolSize", 30) ?: 30
        val maximumPoolSize = if (configuredMaximumPoolSize < minimumIdle) minimumIdle else configuredMaximumPoolSize

        hikariConfig.minimumIdle = minimumIdle
        hikariConfig.maximumPoolSize = maximumPoolSize
        hikariConfig.connectionTimeout = poolConfig?.getLong("connectionTimeout", 10000L) ?: 10000L
        hikariConfig.idleTimeout = poolConfig?.getLong("idleTimeout", 600000L) ?: 600000L
        hikariConfig.maxLifetime = poolConfig?.getLong("maxLifetime", 1800000L) ?: 1800000L

        val leakDetectionThreshold = poolConfig?.getLong("leakDetectionThreshold", 0L) ?: 0L
        if (leakDetectionThreshold > 0) {
            hikariConfig.leakDetectionThreshold = leakDetectionThreshold
        }

        val properties = poolConfig?.getConfigurationSection("properties")
        if (properties != null) {
            for (key in properties.getKeys(false)) {
                val value = properties.get(key)
                if (value != null) {
                    hikariConfig.addDataSourceProperty(key, value)
                }
            }
        }

        hikariDS = HikariDataSource(hikariConfig)
    }

    override fun close() {
        if (hikariDS == null) return

        if (!hikariDS!!.isClosed) hikariDS!!.close()
    }

    override fun getConnection(): Connection {
        try {
            return hikariDS!!.connection
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    override fun evictConnection(connection: Connection) {
        hikariDS!!.evictConnection(connection)
    }
}
