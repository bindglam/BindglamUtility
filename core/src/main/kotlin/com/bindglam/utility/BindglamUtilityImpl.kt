package com.bindglam.utility

import com.bindglam.utility.compatibility.Compatibility
import com.bindglam.utility.compatibility.ItemsAdderCompatibility
import com.bindglam.utility.compatibility.NexoCompatibility
import com.bindglam.utility.database.Database
import com.bindglam.utility.database.MySQLDatabase
import com.bindglam.utility.database.SQLiteDatabase
import com.bindglam.utility.listeners.PlayerListener
import com.bindglam.utility.manager.*
import com.bindglam.utility.messaging.PluginMessenger
import com.bindglam.utility.messaging.PluginMessengerImpl
import com.bindglam.utility.nms.PacketDispatcher
import com.bindglam.utility.version.MinecraftVersion
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.util.*

class BindglamUtilityImpl : JavaPlugin(), BindglamUtilityPlugin {
    private lateinit var compatibility: Compatibility
    private lateinit var pluginMessenger: PluginMessenger
    private lateinit var packetDispatcher: PacketDispatcher
    private lateinit var database: Database
    private lateinit var playerDataManager: PlayerDataManager
    private lateinit var variableParserManager: VariableParserManager
    private lateinit var guiRendererManager: GuiRendererManager

    override fun onEnable() {
        saveDefaultConfig()

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
        packetDispatcher = when(MinecraftVersion.CURRENT_VERSION) {
            MinecraftVersion.V1_21_4 -> com.bindglam.utility.nms.v1_21_R3.PacketDispatcherImpl()
            MinecraftVersion.V1_21_8 -> com.bindglam.utility.nms.v1_21_R5.PacketDispatcherImpl()
            else -> throw IllegalStateException("Unexpected version: ${MinecraftVersion.CURRENT_VERSION}")
        }
        database = when (Objects.requireNonNull(config.getString("database.type"))) {
            "SQLITE" -> SQLiteDatabase()
            "MYSQL" -> MySQLDatabase()
            else -> throw IllegalStateException("Unexpected value: " + config.getString("database.type"))
        }.apply { connect(config.getConfigurationSection("database.${config.getString("database.type")!!.lowercase(Locale.getDefault())}")) }
        playerDataManager = PlayerDataManagerImpl(this)
        variableParserManager = VariableParserManagerImpl()
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

    override fun getCompatibility(): Compatibility {
        return compatibility
    }

    override fun getPluginMessenger(): PluginMessenger {
        return pluginMessenger
    }

    override fun getPacketDispatcher(): PacketDispatcher {
        return packetDispatcher
    }

    override fun getDatabase(): Database {
        return database
    }

    override fun getPlayerDataManager(): PlayerDataManager {
        return playerDataManager
    }

    override fun getVariableParserManager(): VariableParserManager {
        return variableParserManager
    }

    override fun getGuiRendererManager(): GuiRendererManager {
        return guiRendererManager
    }
}