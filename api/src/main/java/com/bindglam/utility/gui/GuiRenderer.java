package com.bindglam.utility.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface GuiRenderer {
    GuiBase getGui();

    @Unmodifiable Set<Player> getViewers();

    void addViewer(Player player);

    void removeViewer(Player player);
}
