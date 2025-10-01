package com.bindglam.utility.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.configuration.ConfigurationSection
import java.sql.Connection
import java.sql.SQLException

class MySQLDatabase : SQLDatabase {
    private var hikariDS: HikariDataSource? = null

    override fun connect(config: ConfigurationSection?) {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
        hikariConfig.jdbcUrl = "jdbc:mysql://" + config!!.getString("host") + ":" + config.getString("port") + "/" + config.getString("database") + "?useUnicode=true&characterEncoding=utf8"
        hikariConfig.username = config.getString("user")
        hikariConfig.password = config.getString("password")
        hikariConfig.connectionTimeout = 30000
        hikariConfig.idleTimeout = 10000
        hikariConfig.minimumIdle = 50 // 100
        hikariConfig.maximumPoolSize = 1000 // 1000

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
