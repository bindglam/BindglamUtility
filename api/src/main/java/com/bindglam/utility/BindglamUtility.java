package com.bindglam.utility;

import com.bindglam.utility.compatibility.Compatibility;
import com.bindglam.utility.database.RedisDatabase;
import com.bindglam.utility.database.SQLDatabase;
import com.bindglam.utility.manager.GuiRendererManager;
import com.bindglam.utility.manager.PlayerDataManager;
import com.bindglam.utility.messaging.PluginMessenger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static SQLDatabase sqlDatabase() {
        return BindglamUtility.instance.getSQLDatabase();
    }

    public static @Nullable RedisDatabase redisDatabase() {
        return BindglamUtility.instance.getRedisDatabase();
    }

    @Deprecated
    public static SQLDatabase database() {
        return sqlDatabase();
    }

    public static PlayerDataManager playerDataManager() {
        return BindglamUtility.instance.getPlayerDataManager();
    }

    public static GuiRendererManager guiRendererManager() {
        return BindglamUtility.instance.getGuiRendererManager();
    }
}
