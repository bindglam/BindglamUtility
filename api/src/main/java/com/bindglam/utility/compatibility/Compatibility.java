package com.bindglam.utility.compatibility;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface Compatibility {
    @Nullable Component getGlyphOrNull(String id, int offsetX);

    @Nullable ItemStack getCustomItemOrNull(String id);

    @Nullable String getCustomItemIdByItemStack(@Nullable ItemStack itemStack);

    default @NotNull Component getGlyph(String id, int offsetX) {
        return Objects.requireNonNull(getGlyphOrNull(id, offsetX));
    }

    default @NotNull ItemStack getCustomItem(String id) {
        return Objects.requireNonNull(getCustomItemOrNull(id));
    }
}
