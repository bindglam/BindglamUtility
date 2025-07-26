package com.bindglam.utility.manager

import com.bindglam.utility.BindglamUtility
import com.bindglam.utility.playerdata.PlayerData
import com.bindglam.utility.playerdata.PlayerDataImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class PlayerDataManagerImpl(private val plugin: Plugin) : PlayerDataManager {
    private val playerDataMap = HashMap<UUID, PlayerData>()

    init {
        try {
            val connection: Connection = BindglamUtility.database().getConnection()
            val statement = connection.createStatement()
            statement.execute("CREATE TABLE IF NOT EXISTS bu_playerdata(uuid VARCHAR(36) PRIMARY KEY, data JSON)")
            statement.close()
            BindglamUtility.database().evictConnection(connection)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        loadAll(true)
    }

    override fun load(uuid: UUID, consumer: Consumer<PlayerData>?, async: Boolean) {
        if (playerDataMap.containsKey(uuid)) return

        val playerData: PlayerData = PlayerDataImpl(plugin, uuid)
        if (async) Bukkit.getAsyncScheduler().runDelayed(plugin, { _ -> playerData.load(consumer, true) }, 20*50L, TimeUnit.MILLISECONDS)
        else playerData.load(consumer, false)
        playerDataMap[uuid] = playerData
    }

    override fun dispose(uuid: UUID, async: Boolean) {
        if (!playerDataMap.containsKey(uuid)) return

        val playerData = playerDataMap[uuid]!!
        if (async) Bukkit.getAsyncScheduler().runNow(plugin) { _ -> playerData.dispose(true) }
        else playerData.dispose(false)
        playerDataMap.remove(uuid)
    }

    override fun getPlayerData(uuid: UUID): PlayerData? {
        return playerDataMap[uuid]
    }
}