package com.bindglam.utility.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GuiRenderer {
    void sendFakeInventory(Player player, Inventory inventory, Component title);
}
