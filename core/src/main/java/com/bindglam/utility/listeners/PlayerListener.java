package com.bindglam.utility.listeners;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.events.BindglamInventoryCloseEvent;
import com.bindglam.utility.gui.GuiBase;
import com.bindglam.utility.playerdata.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.time.Duration;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BindglamUtility.playerDataManager().load(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        BindglamUtility.playerDataManager().dispose(player, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = BindglamUtility.playerDataManager().getPlayerData(player);

        if(playerData.isLoading()) {
            event.setCancelled(true);

            player.showTitle(Title.title(Component.text("데이터 로드 중...").color(NamedTextColor.YELLOW), Component.empty(), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(300))));

            return;
        }
    }

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
