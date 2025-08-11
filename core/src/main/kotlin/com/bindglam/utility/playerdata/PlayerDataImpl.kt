package com.bindglam.utility.playerdata

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.bindglam.utility.BindglamUtility
import com.bindglam.utility.events.BindglamPlayerDataLoadEvent
import com.bindglam.utility.events.BindglamPlayerDataSaveEvent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

class PlayerDataImpl(private val plugin: Plugin, private val uuid: UUID) : PlayerData {
    private val variables = HashMap<NamespacedKey, Any?>()

    private val isLoading = AtomicBoolean(true)

    override fun load(consumer: Consumer<PlayerData>?, async: Boolean) {
        val player = player

        try {
            val connection: Connection = BindglamUtility.database().getConnection()
            val statement = connection.prepareStatement("SELECT * FROM bu_playerdata WHERE uuid = ?")
            statement.setString(1, uuid.toString())
            val rs = statement.executeQuery()

            while (rs.next()) {
                val dataJson = JSON.parse(rs.getString("data")) as JSONObject

                dataJson.forEach { (name: String, valueJson: Any) -> variables[NamespacedKey.fromString(name)!!] = BindglamUtility.getInstance().variableParserManager.parseFromJSON(valueJson) }
            }

            rs.close()
            statement.close()
            BindglamUtility.database().evictConnection(connection)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        isLoading.set(false)

        if (player != null)
            BindglamPlayerDataLoadEvent(player, this, async).callEvent()

        consumer?.accept(this)
    }

    override fun dispose(async: Boolean) {
        if (async) Bukkit.getAsyncScheduler().runNow(plugin) { _ -> disposeInternal(true) }
        else disposeInternal(false)
    }

    private fun disposeInternal(async: Boolean) {
        try {
            val player = player

            if (player != null) BindglamPlayerDataSaveEvent(player, this, async).callEvent()

            val dataJson = JSONObject()

            variables.forEach { (key: NamespacedKey, value: Any?) ->
                dataJson[key.toString()] = BindglamUtility.getInstance().variableParserManager.parseToJSON(value)
            }

            val connection: Connection = BindglamUtility.database().getConnection()
            val statement1 = connection.prepareStatement("SELECT * FROM bu_playerdata WHERE uuid = ?")
            statement1.setString(1, uuid.toString())
            val rs1 = statement1.executeQuery()

            if (rs1.next()) {
                val statement2 = connection.prepareStatement("UPDATE bu_playerdata SET data = ? WHERE uuid = ?")
                statement2.setString(1, dataJson.toString())
                statement2.setString(2, uuid.toString())
                statement2.executeUpdate()
                statement2.close()
            } else {
                val statement2 = connection.prepareStatement("INSERT INTO bu_playerdata(uuid, data) VALUES (?, ?)")
                statement2.setString(1, uuid.toString())
                statement2.setString(2, dataJson.toString())
                statement2.executeUpdate()
                statement2.close()
            }

            rs1.close()
            statement1.close()
            BindglamUtility.database().evictConnection(connection)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    override fun getUniqueId(): UUID {
        return uuid
    }

    override fun <T : Any?> getVariable(key: NamespacedKey): T? = variables[key] as T?

    @Deprecated("Deprecated in Java")
    override fun <T> getVariable(name: String): T? = variables[NamespacedKey(plugin, name)] as T?

    override fun setVariable(key: NamespacedKey, value: Any?) {
        variables[key] = value
    }

    @Deprecated("Deprecated in Java")
    override fun setVariable(name: String, value: Any?) {
        variables[NamespacedKey(plugin, name)] = value
    }

    override fun hasVariable(key: NamespacedKey): Boolean = variables.containsKey(key)

    @Deprecated("Deprecated in Java")
    override fun hasVariable(name: String): Boolean = variables.containsKey(NamespacedKey(plugin, name))

    override fun removeVariable(key: NamespacedKey) {
        variables.remove(key)
    }

    @Deprecated("Deprecated in Java")
    override fun removeVariable(name: String) {
        variables.remove(NamespacedKey(plugin, name))
    }

    override fun getVariables(): Map<NamespacedKey, Any?> {
        return java.util.Map.copyOf(variables)
    }

    override fun isLoading(): Boolean {
        return isLoading.get()
    }

    override fun getPlayer(): Player? {
        return Bukkit.getPlayer(uuid)
    }
}