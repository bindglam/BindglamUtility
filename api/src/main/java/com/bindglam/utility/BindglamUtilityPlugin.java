package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.database.RedisDatabase;
import com.bindglam.utility.database.SQLDatabase;
import com.bindglam.utility.manager.GuiRendererManager;
import com.bindglam.utility.manager.PlayerDataManager;
import com.bindglam.utility.manager.VariableParserManager;
import com.bindglam.utility.messaging.PluginMessenger;
import com.bindglam.utility.nms.PacketDispatcher;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public interface BindglamUtilityPlugin {
    JavaPlugin getJavaPlugin();

    Compatibility getCompatibility();

    PluginMessenger getPluginMessenger();

    PacketDispatcher getPacketDispatcher();

    SQLDatabase getSQLDatabase();

    @Nullable RedisDatabase getRedisDatabase();

    PlayerDataManager getPlayerDataManager();

    VariableParserManager getVariableParserManager();

    GuiRendererManager getGuiRendererManager();
}
