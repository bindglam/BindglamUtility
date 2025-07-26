package com.bindglam.utility.manager

import com.bindglam.utility.gui.GuiBase
import com.bindglam.utility.gui.GuiRenderer
import com.bindglam.utility.gui.GuiRendererImpl
import org.bukkit.plugin.Plugin

class GuiRendererManagerImpl(private val plugin: Plugin) : GuiRendererManager {
    override fun createRenderer(gui: GuiBase): GuiRenderer {
        return GuiRendererImpl(plugin, gui)
    }
}