package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.database.Database;
import com.bindglam.utility.manager.GuiRendererManager;
import com.bindglam.utility.manager.PlayerDataManager;
import com.bindglam.utility.messaging.PluginMessenger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class BindglamUtility {
    private static BindglamUtilityPlugin instance;

    private BindglamUtility() {
    }

    public static @NotNull BindglamUtilityPlugin getInstance() {
        return Objects.requireNonNull(instance);
    }

    @ApiStatus.Internal
    static void setInstance(@NotNull BindglamUtilityPlugin instance) {
        BindglamUtility.instance = instance;
    }

    public static Compatibility compatibility() {
        return BindglamUtility.instance.getCompatibility();
    }

    public static PluginMessenger pluginMessenger() {
        return BindglamUtility.instance.getPluginMessenger();
    }

    public static Database database() {
        return BindglamUtility.instance.getDatabase();
    }

    public static PlayerDataManager playerDataManager() {
        return BindglamUtility.instance.getPlayerDataManager();
    }

    public static GuiRendererManager guiRendererManager() {
        return BindglamUtility.instance.getGuiRendererManager();
    }
}
