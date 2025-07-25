package com.bindglam.utility.events;

import com.bindglam.utility.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class BindglamPlayerDataSaveEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerData playerData;

    public BindglamPlayerDataSaveEvent(@NotNull Player who, @NotNull PlayerData playerData, boolean async) {
        super(who, async);

        this.playerData = playerData;
    }

    public @NotNull PlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
