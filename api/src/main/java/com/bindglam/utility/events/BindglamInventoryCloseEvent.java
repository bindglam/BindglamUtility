package com.bindglam.utility.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class BindglamInventoryCloseEvent extends InventoryCloseEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public BindglamInventoryCloseEvent(@NotNull InventoryView transaction) {
        super(transaction);
    }

    public BindglamInventoryCloseEvent(@NotNull InventoryView transaction, @NotNull Reason reason) {
        super(transaction, reason);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
