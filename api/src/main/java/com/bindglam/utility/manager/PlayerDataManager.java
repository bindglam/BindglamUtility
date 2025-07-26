package com.bindglam.utility.manager;

import com.bindglam.utility.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerDataManager {
    void load(@NotNull UUID uuid, @Nullable Consumer<PlayerData> consumer, boolean async);

    void dispose(@NotNull UUID uuid, boolean async);

    @Nullable PlayerData getPlayerData(@NotNull UUID uuid);

    default void load(UUID uuid, Consumer<PlayerData> consumer) {
        load(uuid, consumer, true);
    }

    default void load(UUID uuid) {
        load(uuid, null);
    }

    default void load(Player player) {
        load(player.getUniqueId());
    }

    default void dispose(UUID uuid) {
        dispose(uuid, true);
    }

    default void dispose(Player player, boolean async) {
        dispose(player.getUniqueId(), async);
    }

    default void dispose(Player player) {
        dispose(player, true);
    }

    default void loadAll(boolean async) {
        Bukkit.getOnlinePlayers().forEach((player) -> load(player.getUniqueId(), null, async));
    }

    default void disposeAll(boolean async) {
        Bukkit.getOnlinePlayers().forEach((player) -> dispose(player, async));
    }

    default @Nullable PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }
}
