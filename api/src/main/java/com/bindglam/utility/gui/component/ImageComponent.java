package com.bindglam.utility.gui.component;

import com.bindglam.utility.BindglamUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ImageComponent extends UIComponent {
    private String glyphId;
    private TextColor color = NamedTextColor.WHITE;

    public ImageComponent(String glyphId) {
        this.glyphId = glyphId;
    }

    public String getGlyphId() {
        return glyphId;
    }

    public void setGlyphId(String glyphId) {
        this.glyphId = glyphId;
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }

    @Override
    public Component build(int offset) {
        String id = getAnimator().getGlyph();
        if(id == null)
            id = glyphId;

        TextColor glyphColor = getAnimator().getColor();
        if(glyphColor == null)
            glyphColor = color;

        return BindglamUtility.getInstance().getCompatibility().getGlyph(id, getOffset()+getAnimator().getOffset()+offset).color(glyphColor);
    }

    @Override
    public int width() {
        return BindglamUtility.compatibility().getGlyphWidth(glyphId);
    }
}
