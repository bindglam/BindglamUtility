package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.compatibility.ItemsAdderCompatibility;
import com.bindglam.utility.compatibility.NexoCompatibility;
import com.bindglam.utility.database.Database;
import com.bindglam.utility.database.MySQLDatabase;
import com.bindglam.utility.database.SQLiteDatabase;
import com.bindglam.utility.gui.GuiRenderer;
import com.bindglam.utility.listeners.PlayerListener;
import com.bindglam.utility.managers.PlayerDataManager;
import com.bindglam.utility.pluginmessaging.PluginMessenger;
import com.bindglam.utility.version.MinecraftVersion;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BindglamUtility extends JavaPlugin {
    private static BindglamUtility instance;

    private Compatibility compatibility;
    private PluginMessenger pluginMessenger;
    private GuiRenderer guiRenderer;
    private Database database;
    private PlayerDataManager playerDataManager;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        saveDefaultConfig();

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
        if(MinecraftVersion.CURRENT_VERSION.equals(MinecraftVersion.V1_21_4)) {
            guiRenderer = new com.bindglam.utility.nms.v1_21_R3.GuiRendererImpl();
        } else if(MinecraftVersion.CURRENT_VERSION.equals(MinecraftVersion.V1_21_8)) {
            guiRenderer = new com.bindglam.utility.nms.v1_21_R5.GuiRendererImpl();
        } else {
            getLogger().severe("Unsupported version is detected! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        database = switch(Objects.requireNonNull(getConfig().getString("database.type"))) {
            case "SQLITE" -> new SQLiteDatabase();
            case "MYSQL" -> new MySQLDatabase();
            default -> throw new IllegalStateException("Unexpected value: " + getConfig().getString("database.type"));
        };
        playerDataManager = new PlayerDataManager();

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

        database.connect(getConfig().getConfigurationSection("database." + Objects.requireNonNull(getConfig().getString("database.type")).toLowerCase()));

        playerDataManager.init();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        playerDataManager.disposeAll(false);

        database.close();
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

    public Database getDatabase() {
        return database;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
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

    public static Database database() {
        return getInstance().getDatabase();
    }

    public static PlayerDataManager playerDataManager() {
        return getInstance().getPlayerDataManager();
    }
}
