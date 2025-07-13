package com.bindglam.utility.compatibility;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Compatibility {
    @NotNull Component getGlyph(String id, int offsetX);

    @NotNull ItemStack getCustomItem(String id);
}
