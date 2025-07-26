package com.bindglam.utility.listeners

import com.bindglam.utility.BindglamUtility
import com.bindglam.utility.BindglamUtilityImpl
import com.bindglam.utility.events.BindglamInventoryCloseEvent
import com.bindglam.utility.gui.GuiBase
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.time.Duration

class PlayerListener(private val plugin: Plugin) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        BindglamUtility.playerDataManager().load(player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        BindglamUtility.playerDataManager().dispose(player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val playerData = BindglamUtility.playerDataManager().getPlayerData(player) ?: return

        if (playerData.isLoading()) {
            event.isCancelled = true

            player.showTitle(Title.title(Component.text("데이터 로드 중...").color(NamedTextColor.YELLOW), Component.empty(), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(300))))

            return
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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