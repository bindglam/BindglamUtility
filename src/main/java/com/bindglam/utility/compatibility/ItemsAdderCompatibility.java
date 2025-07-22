package com.bindglam.utility.compatibility;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemsAdderCompatibility implements Compatibility {
    @Override
    public @NotNull Component getGlyph(String id, int offsetX) {
        return Component.text(FontImageWrapper.instance(id).setOffset(offsetX).getString());
    }

    @Override
    public @Nullable Component getGlyphOrNull(String id, int offsetX) {
        if(!FontImageWrapper.getNamespacedIdsInRegistry().contains(id))
            return null;
        return getGlyph(id, offsetX);
    }

    @Override
    public @NotNull ItemStack getCustomItem(String id) {
        return CustomStack.getInstance(id).getItemStack();
    }
}
