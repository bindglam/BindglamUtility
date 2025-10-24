package com.bindglam.utility.gui

import com.bindglam.utility.BindglamUtility
import com.bindglam.utility.events.BindglamInventoryCloseEvent
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.Unmodifiable
import java.util.*
import java.util.concurrent.TimeUnit

class GuiRendererImpl(private val plugin: Plugin, private val gui: GuiBase) : GuiRenderer, Listener {
    private val viewers = HashSet<UUID>()

    private val tickTask: Int
    private val uiUpdateTask: ScheduledTask

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)

        tickTask = if(gui.tickInterval > 0) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
                gui.onTick()
            }, 0L, gui.tickInterval.toLong())
        } else -1

        uiUpdateTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { task ->
            var shouldUpdateUI = gui.isChanged

            val list = gui.uiComponents.values.stream().toList()
            for (i in gui.uiComponents.size - 1 downTo 0) {
                val component = list[i]

                if (component.animator.animation != null) {
                    shouldUpdateUI = true

                    component.animator.update()
                }
            }

            if (shouldUpdateUI) {
                updateUIComponent()

                gui.isChanged = false
            }
        }, 50L, 50L, TimeUnit.MILLISECONDS)
    }

    private fun updateUIComponent() {
        var title = gui.title

        var offset = -gui.titleGlyphWidth
        gui.uiComponents.values.forEach { component -> {
            title = title.append(component.build(offset))

            offset -= component.width()
        } }

        for (player in getViewers()) {
            if (player == null) continue

            BindglamUtility.getInstance().packetDispatcher.sendFakeInventory(player, gui.inventory, title)
        }
    }

    @EventHandler
    fun onOpen(event: InventoryOpenEvent) {
        val inventory = event.view.topInventory
        if (inventory.getHolder(false) != gui) return

        gui.onOpen(event)

        Bukkit.getScheduler().runTaskLater(plugin, Runnable { updateUIComponent() }, 2L);
    }

    @EventHandler
    fun onClickEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val inventory = event.view.topInventory
        if (inventory.getHolder(false) != gui) return

        gui.onClick(event)
    }

    @EventHandler
    fun onCloseEvent(event: BindglamInventoryCloseEvent) {
        val player = event.player as Player
        val inventory = event.view.topInventory
        if (inventory.getHolder(false) != gui) return

        gui.onClose(event as InventoryCloseEvent)
        gui.onClose(event)

        if (event.isCancelled) return

        if (tickTask != -1)
            Bukkit.getScheduler().cancelTask(tickTask)
        uiUpdateTask.cancel()

        Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.updateInventory() }, 1L)
        HandlerList.unregisterAll(this)

        removeViewer(player)
    }

    override fun getGui(): GuiBase {
        return gui
    }

    override fun getViewers(): @Unmodifiable Set<Player?> {
        return HashSet(viewers.stream().map { uuid: UUID -> Bukkit.getPlayer(uuid) }.toList())
    }

    override fun addViewer(player: Player) {
        viewers.add(player.uniqueId)
    }

    override fun removeViewer(player: Player) {
        viewers.remove(player.uniqueId)
    }
}