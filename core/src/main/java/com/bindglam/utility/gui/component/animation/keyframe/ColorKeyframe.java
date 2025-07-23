package com.bindglam.utility.gui.component.animation.keyframe;

import net.kyori.adventure.text.format.TextColor;

public class ColorKeyframe extends AbstractKeyframe<TextColor> {
    public ColorKeyframe(KeyframeType type, TextColor color) {
        super(type, color);
    }
}
