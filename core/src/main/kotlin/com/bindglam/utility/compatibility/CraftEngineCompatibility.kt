package com.bindglam.utility.compatibility

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.momirealms.craftengine.bukkit.api.CraftEngineImages
import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.core.plugin.CraftEngine
import net.momirealms.craftengine.core.util.Key
import org.bukkit.inventory.ItemStack

class CraftEngineCompatibility : Compatibility {
    override fun getGlyphOrNull(id: String, offsetX: Int): Component? {
        return CraftEngineImages.byId(id.key())?.let {
            Component.text()
                .content(CraftEngine.instance().fontManager().createRawOffsets(offsetX))
                .color(NamedTextColor.WHITE)
                .append(Component.text(String(Character.toChars(it.codepointAt(0, 0)))).font(net.kyori.adventure.key.Key.key(it.font().toString())))
                .build()
        }
    }

    override fun getCustomItemOrNull(id: String): ItemStack? {
        return CraftEngineItems.byId(id.key())?.buildItemStack()
    }

    override fun getCustomItemIdByItemStack(itemStack: ItemStack?): String? {
        return itemStack?.let { CraftEngineItems.byItemStack(it) }?.id()?.toString()
    }

    private fun String.key() = if (contains(':')) Key.of(substringBefore(':'), substringAfter(':')) else Key.of(this)
}