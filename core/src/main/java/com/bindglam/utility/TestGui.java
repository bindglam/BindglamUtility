package com.bindglam.utility;

import com.bindglam.utility.gui.GuiBase;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TestGui extends GuiBase {
    public TestGui() {
        super(9*6, Component.text("테스트"), 1);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.getWhoClicked().sendMessage(Component.text(event.getRawSlot()));
    }

    @Override
    public void onTick() {
        System.out.println("test");
    }
}
