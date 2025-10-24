package com.bindglam.utility.compatibility

import dev.lone.itemsadder.api.CustomStack
import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

class ItemsAdderCompatibility : Compatibility {
    override fun getGlyphOrNull(id: String, offsetX: Int): Component? {
        if (!FontImageWrapper.getNamespacedIdsInRegistry().contains(id))
            return null

        return Component.text(FontImageWrapper.instance(id).setOffset(offsetX).string)
    }

    override fun getGlyphWidth(id: String): Int {
        if (!FontImageWrapper.getNamespacedIdsInRegistry().contains(id))
            return 0

        return FontImageWrapper.instance(id).width
    }

    override fun getCustomItemOrNull(id: String): ItemStack? {
        if (!CustomStack.isInRegistry(id))
            return null

        return CustomStack.getInstance(id)!!.itemStack
    }

    override fun getCustomItemIdByItemStack(itemStack: ItemStack?): String? {
        return CustomStack.byItemStack(itemStack)?.namespacedID
    }
}