package com.bindglam.utility.compatibility

import com.nexomc.nexo.NexoPlugin
import com.nexomc.nexo.api.NexoItems
import com.nexomc.nexo.glyphs.Shift
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

class NexoCompatibility : Compatibility {
    override fun getGlyph(id: String, offsetX: Int): Component {
        return Component.text(Shift.of(offsetX)).append(NexoPlugin.instance().fontManager().glyphFromID(id)!!.glyphComponent(true)).color(NamedTextColor.WHITE)
    }

    override fun getGlyphOrNull(id: String, offsetX: Int): Component? {
        if (NexoPlugin.instance().fontManager().glyphFromID(id) == null) return null
        return getGlyph(id, offsetX)
    }

    override fun getCustomItem(id: String): ItemStack {
        return NexoItems.itemFromId(id)!!.build()
    }

    override fun getCustomItemOrNull(id: String): ItemStack? {
        if (!NexoItems.exists(id)) return null
        return getCustomItem(id)
    }
}