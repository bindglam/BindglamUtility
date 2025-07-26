package com.bindglam.utility.nms.v1_21_R5

import com.bindglam.utility.gui.GuiRenderer
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class GuiRendererImpl : GuiRenderer {
    override fun sendFakeInventory(player: Player, inventory: Inventory, title: Component) {
        val nmsPlayer = (player as CraftPlayer).handle

        val nmsContents = NonNullList.create<ItemStack>()
        inventory.contents.forEach { bukkitItemStack ->
            if(bukkitItemStack == null) {
                nmsContents.add(ItemStack.EMPTY)
                return@forEach
            }
            nmsContents.add((bukkitItemStack as CraftItemStack).handle)
        }

        val menuType = when(inventory.size) {
            9 -> MenuType.GENERIC_9x1
            9*2 -> MenuType.GENERIC_9x2
            9*3 -> MenuType.GENERIC_9x3
            9*4 -> MenuType.GENERIC_9x4
            9*5 -> MenuType.GENERIC_9x5
            9*6 -> MenuType.GENERIC_9x6
            else -> throw RuntimeException()
        }

        val openPacket = ClientboundOpenScreenPacket(nmsPlayer.containerMenu.containerId, menuType, PaperAdventure.asVanilla(title))
        val setContentsPacket = ClientboundContainerSetContentPacket(nmsPlayer.containerMenu.containerId, nmsPlayer.containerMenu.incrementStateId(), nmsContents, ItemStack.EMPTY)

        nmsPlayer.connection.send(openPacket)
        nmsPlayer.connection.send(setContentsPacket)
    }
}