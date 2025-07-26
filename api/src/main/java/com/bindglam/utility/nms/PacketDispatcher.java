package com.bindglam.utility.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface PacketDispatcher {
    void sendFakeInventory(Player player, Inventory inventory, Component title);
}
