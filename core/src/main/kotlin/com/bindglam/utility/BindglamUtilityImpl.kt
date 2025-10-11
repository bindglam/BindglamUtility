package com.bindglam.utility

import com.bindglam.utility.compatibility.Compatibility
import com.bindglam.utility.compatibility.ItemsAdderCompatibility
import com.bindglam.utility.compatibility.NexoCompatibility
import com.bindglam.utility.database.MySQLDatabase
import com.bindglam.utility.database.RedisDatabase
import com.bindglam.utility.database.RedisDatabaseImpl
import com.bindglam.utility.database.SQLDatabase
import com.bindglam.utility.database.SQLiteDatabase
import com.bindglam.utility.listeners.PlayerListener
import com.bindglam.utility.manager.*
import com.bindglam.utility.messaging.PluginMessenger
import com.bindglam.utility.messaging.PluginMessengerImpl
import com.bindglam.utility.nms.PacketDispatcher
import com.bindglam.utility.utils.MinecraftVersion
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.util.*

class BindglamUtilityImpl : JavaPlugin(), BindglamUtilityPlugin {
    private lateinit var compatibility: Compatibility
    private lateinit var pluginMessenger: PluginMessenger
    private lateinit var packetDispatcher: PacketDispatcher
    private lateinit var sqlDatabase: SQLDatabase
    private var redisDatabase: RedisDatabase? = null
    private lateinit var guiRendererManager: GuiRendererManager

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))

        registerCommands()
    }

    override fun onEnable() {
        saveDefaultConfig()

        CommandAPI.onEnable()

        BindglamUtility.setInstance(this)

        compatibility = if (server.pluginManager.isPluginEnabled("ItemsAdder")) {
            ItemsAdderCompatibility()
        } else if (server.pluginManager.isPluginEnabled("Nexo")) {
            NexoCompatibility()
        } else {
            logger.severe("ItemsAdder or Nexo is not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
            return
        }
        pluginMessenger = PluginMessengerImpl(this)
        packetDispatcher = when(MinecraftVersion.CURRENT) {
            MinecraftVersion.V1_21_4 -> com.bindglam.utility.nms.v1_21_R3.PacketDispatcherImpl()
            MinecraftVersion.V1_21_8 -> com.bindglam.utility.nms.v1_21_R5.PacketDispatcherImpl()
            else -> throw IllegalStateException("Unexpected version: ${MinecraftVersion.CURRENT}")
        }
        sqlDatabase = when (Objects.requireNonNull(config.getString("database.type"))) {
            "SQLITE" -> SQLiteDatabase()
            "MYSQL" -> MySQLDatabase()
            else -> throw IllegalStateException("Unexpected value: " + config.getString("database.type"))
        }.apply { connect(config.getConfigurationSection("database.${config.getString("database.type")!!.lowercase(Locale.getDefault())}")) }
        redisDatabase = if(config.getBoolean("database.redis.enabled")) RedisDatabaseImpl().also { it.connect(config.getConfigurationSection("database.redis")) } else null
        guiRendererManager = GuiRendererManagerImpl(this)

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        server.messenger.registerIncomingPluginChannel(this, "BungeeCord", pluginMessenger as PluginMessageListener)

        server.pluginManager.registerEvents(PlayerListener(this), this)

        logger.info(
            """
                
                ______ _           _       _                 _       _   _ _   _ _ _ _         
                | ___ (_)         | |     | |               ( )     | | | | | (_) (_) |        
                | |_/ /_ _ __   __| | __ _| | __ _ _ __ ___ |/ ___  | | | | |_ _| |_| |_ _   _ 
                | ___ \ | '_ \ / _` |/ _` | |/ _` | '_ ` _ \  / __| | | | | __| | | | __| | | |
                | |_/ / | | | | (_| | (_| | | (_| | | | | | | \__ \ | |_| | |_| | | | |_| |_| |
                \____/|_|_| |_|\__,_|\__, |_|\__,_|_| |_| |_| |___/  \___/ \__|_|_|_|\__|\__, |
                                      __/ |                                               __/ |
                                     |___/                                               |___/ 
                
                """.trimIndent()
        )
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    private fun registerCommands() {
        CommandAPICommand("binglamutility")
            .withAliases("bu")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
            )
            .register()
    }

    override fun getJavaPlugin(): JavaPlugin = this
    override fun getCompatibility(): Compatibility = compatibility
    override fun getPluginMessenger(): PluginMessenger = pluginMessenger
    override fun getPacketDispatcher(): PacketDispatcher = packetDispatcher
    override fun getSQLDatabase(): SQLDatabase = sqlDatabase
    override fun getRedisDatabase(): RedisDatabase? = redisDatabase
    override fun getGuiRendererManager(): GuiRendererManager = guiRendererManager
}