package com.bindglam.utility.compatibility;

import com.nexomc.nexo.NexoPlugin;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.glyphs.Shift;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NexoCompatibility implements Compatibility {
    @Override
    public @Nullable Component getGlyphOrNull(String id, int offsetX) {
        if(NexoPlugin.instance().fontManager().glyphFromID(id) == null)
            return null;

        return Component.text(Shift.INSTANCE.of(offsetX)).append(Objects.requireNonNull(NexoPlugin.instance().fontManager().glyphFromID(id)).glyphComponent(true)).color(NamedTextColor.WHITE);
    }

    @Override
    public @Nullable ItemStack getCustomItemOrNull(String id) {
        if(!NexoItems.exists(id))
            return null;

        return Objects.requireNonNull(NexoItems.itemFromId(id)).build();
    }

    @Override
    public @Nullable String getCustomItemIdByItemStack(@Nullable ItemStack itemStack) {
        return NexoItems.idFromItem(itemStack);
    }
}
