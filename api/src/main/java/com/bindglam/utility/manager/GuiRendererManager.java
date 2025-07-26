package com.bindglam.utility.manager;

import com.bindglam.utility.gui.GuiBase;
import com.bindglam.utility.gui.GuiRenderer;
import org.jetbrains.annotations.NotNull;

public interface GuiRendererManager {
    @NotNull GuiRenderer createRenderer(@NotNull GuiBase gui);
}
