package com.bindglam.utility.compatibility;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Compatibility {
    @NotNull Component getGlyph(String id, int offsetX);

    @Nullable Component getGlyphOrNull(String id, int offsetX);

    @NotNull ItemStack getCustomItem(String id);

    @Nullable ItemStack getCustomItemOrNull(String id);
}
