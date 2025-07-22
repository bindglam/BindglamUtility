package com.bindglam.utility.compatibility;

import com.nexomc.nexo.NexoPlugin;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.glyphs.Shift;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NexoCompatibility implements Compatibility {
    @Override
    public @NotNull Component getGlyph(String id, int offsetX) {
        return Component.text(Shift.INSTANCE.of(offsetX)).append(Objects.requireNonNull(NexoPlugin.instance().fontManager().glyphFromID(id)).glyphComponent());
    }

    @Override
    public @Nullable Component getGlyphOrNull(String id, int offsetX) {
        if(NexoPlugin.instance().fontManager().glyphFromID(id) == null)
            return null;
        return getGlyph(id, offsetX);
    }

    @Override
    public @NotNull ItemStack getCustomItem(String id) {
        return Objects.requireNonNull(NexoItems.itemFromId(id)).build();
    }
}
