package com.bindglam.utility.compatibility

import dev.lone.itemsadder.api.CustomStack
import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

class ItemsAdderCompatibility : Compatibility {
    override fun getGlyph(id: String, offsetX: Int): Component {
        return Component.text(FontImageWrapper.instance(id).setOffset(offsetX).string)
    }

    override fun getGlyphOrNull(id: String, offsetX: Int): Component? {
        if (!FontImageWrapper.getNamespacedIdsInRegistry().contains(id)) return null
        return getGlyph(id, offsetX)
    }

    override fun getCustomItem(id: String): ItemStack {
        return CustomStack.getInstance(id)!!.itemStack
    }

    override fun getCustomItemOrNull(id: String): ItemStack? {
        if (!CustomStack.isInRegistry(id)) return null
        return getCustomItem(id)
    }
}