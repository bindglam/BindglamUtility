package com.bindglam.utility.listeners;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.events.BindglamInventoryCloseEvent;
import com.bindglam.utility.gui.GuiBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Objects;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder(false);
        if(holder instanceof GuiBase gui)
            gui.removeViewer(player);

        BindglamInventoryCloseEvent closeEvent = new BindglamInventoryCloseEvent(event.getView(), event.getReason());

        closeEvent.callEvent();

        if(closeEvent.isCancelled() && event.getReason() != InventoryCloseEvent.Reason.DEATH && event.getReason() != InventoryCloseEvent.Reason.DISCONNECT) {
            Bukkit.getScheduler().runTaskLater(BindglamUtility.getInstance(), () -> {
                if(holder != null) {
                    player.openInventory(holder.getInventory());

                    if(holder instanceof GuiBase gui)
                        gui.addViewer(player);
                } else {
                    player.openInventory(inventory);
                }
            }, 1L);
        }
    }
}
