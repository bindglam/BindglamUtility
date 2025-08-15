package com.bindglam.utility.playerdata;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerData {
    void load(Consumer<PlayerData> consumer, boolean async);

    void dispose(boolean async);

    UUID getUniqueId();

    @NotNull VariableHolder getVariableHolder();

    boolean isLoading();

    @Nullable Player getPlayer();
}
