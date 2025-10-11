package com.bindglam.utility.listeners

import com.bindglam.utility.events.BindglamInventoryCloseEvent
import com.bindglam.utility.gui.GuiBase
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin

class PlayerListener(private val plugin: Plugin) : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        val inventory = event.inventory
        val holder = inventory.getHolder(false)
        if (holder is GuiBase) holder.renderer?.removeViewer(player)

        val closeEvent = BindglamInventoryCloseEvent(event.view, event.reason)

        closeEvent.callEvent()

        if (closeEvent.isCancelled && event.reason != InventoryCloseEvent.Reason.DEATH && event.reason != InventoryCloseEvent.Reason.DISCONNECT) {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                if (holder != null) {
                    player.openInventory(holder.inventory)

                    if (holder is GuiBase) holder.renderer?.addViewer(player)
                } else {
                    player.openInventory(inventory)
                }
            }, 1L)
        }
    }
}