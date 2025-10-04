package com.bindglam.utility.database

import org.bukkit.configuration.ConfigurationSection
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

class RedisDatabaseImpl : RedisDatabase {
    private val poolConfig = JedisPoolConfig()

    private lateinit var pool: JedisPool

    override fun connect(config: ConfigurationSection?) {
        config ?: return

        pool = JedisPool(poolConfig, config.getString("host"), config.getInt("port"), 3000, config.getString("password"))
    }

    override fun close() {
        pool.close()
    }

    override fun getResource(): JedisWrapper = JedisWrapper(pool.resource)
}