package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.compatibility.ItemsAdderCompatibility;
import com.bindglam.utility.compatibility.NexoCompatibility;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BindglamUtility extends JavaPlugin {
    private static BindglamUtility instance;

    private Compatibility compatibility;

    @Override
    public void onEnable() {
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
    }

    public Compatibility getCompatibility() {
        return compatibility;
    }

    public static @NotNull BindglamUtility getInstance() {
        return instance;
    }
}
