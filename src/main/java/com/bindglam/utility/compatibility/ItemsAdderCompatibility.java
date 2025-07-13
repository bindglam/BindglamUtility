package com.bindglam.utility.compatibility;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemsAdderCompatibility implements Compatibility {
    @Override
    public @NotNull Component getGlyph(String id, int offsetX) {
        return Component.text(FontImageWrapper.instance(id).setOffset(offsetX).getString());
    }

    @Override
    public @NotNull ItemStack getCustomItem(String id) {
        return CustomStack.getInstance(id).getItemStack();
    }
}
