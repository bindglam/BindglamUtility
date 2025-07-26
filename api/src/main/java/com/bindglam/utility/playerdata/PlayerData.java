package com.bindglam.utility.playerdata;

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

    <T> @Nullable T getVariable(@NotNull String name);

    void setVariable(@NotNull String name, @Nullable Object value);

    boolean hasVariable(@NotNull String name);

    void removeVariable(@NotNull String name);

    Map<String, Object> getVariables();

    boolean isLoading();

    @Nullable Player getPlayer();
}
