package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.database.Database;
import com.bindglam.utility.manager.GuiRendererManager;
import com.bindglam.utility.manager.PlayerDataManager;
import com.bindglam.utility.manager.VariableParserManager;
import com.bindglam.utility.messaging.PluginMessenger;
import com.bindglam.utility.nms.PacketDispatcher;

public interface BindglamUtilityPlugin {
    Compatibility getCompatibility();

    PluginMessenger getPluginMessenger();

    PacketDispatcher getPacketDispatcher();

    Database getDatabase();

    PlayerDataManager getPlayerDataManager();

    VariableParserManager getVariableParserManager();

    GuiRendererManager getGuiRendererManager();
}
