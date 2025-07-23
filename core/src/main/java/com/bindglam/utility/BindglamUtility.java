package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.compatibility.ItemsAdderCompatibility;
import com.bindglam.utility.compatibility.NexoCompatibility;
import com.bindglam.utility.gui.GuiRenderer;
import com.bindglam.utility.listeners.PlayerListener;
import com.bindglam.utility.pluginmessaging.PluginMessenger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BindglamUtility extends JavaPlugin {
    private static BindglamUtility instance;

    private Compatibility compatibility;
    private PluginMessenger pluginMessenger;
    private GuiRenderer guiRenderer;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        instance = this;

        if(getServer().getPluginManager().isPluginEnabled("ItemsAdder")) {
            compatibility = new ItemsAdderCompatibility();
        } else if(getServer().getPluginManager().isPluginEnabled("Nexo")) {
            compatibility = new NexoCompatibility();
        } else {
            getLogger().severe("ItemsAdder or Nexo is not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        pluginMessenger = new PluginMessenger();
        guiRenderer = new com.bindglam.utility.nms.v1_21_R3.GuiRendererImpl();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessenger);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLogger().info("""
                
                ______ _           _       _                 _       _   _ _   _ _ _ _        \s
                | ___ (_)         | |     | |               ( )     | | | | | (_) (_) |       \s
                | |_/ /_ _ __   __| | __ _| | __ _ _ __ ___ |/ ___  | | | | |_ _| |_| |_ _   _\s
                | ___ \\ | '_ \\ / _` |/ _` | |/ _` | '_ ` _ \\  / __| | | | | __| | | | __| | | |
                | |_/ / | | | | (_| | (_| | | (_| | | | | | | \\__ \\ | |_| | |_| | | | |_| |_| |
                \\____/|_|_| |_|\\__,_|\\__, |_|\\__,_|_| |_| |_| |___/  \\___/ \\__|_|_|_|\\__|\\__, |
                                      __/ |                                               __/ |
                                     |___/                                               |___/\s
                """);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public Compatibility getCompatibility() {
        return compatibility;
    }

    public PluginMessenger getPluginMessenger() {
        return pluginMessenger;
    }

    public GuiRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public static @NotNull BindglamUtility getInstance() {
        return instance;
    }

    public static Compatibility compatibility() {
        return getInstance().getCompatibility();
    }

    public static PluginMessenger pluginMessenger() {
        return getInstance().getPluginMessenger();
    }

    public static GuiRenderer guiRenderer() {
        return getInstance().getGuiRenderer();
    }
}
