package com.bindglam.utility.gui.component;

import com.bindglam.utility.BindglamUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class ImageComponent extends OffsetComponent {
    private String glyphId;

    public ImageComponent(String glyphId) {
        this.glyphId = glyphId;
    }

    public String getGlyphId() {
        return glyphId;
    }

    public void setGlyphId(String glyphId) {
        this.glyphId = glyphId;
    }

    @Override
    public Component build(TextColor color) {
        String id = getAnimator().getGlyph();
        if(id == null)
            id = glyphId;

        return BindglamUtility.getInstance().getCompatibility().getGlyph(id, getOffset()+getAnimator().getOffset()).color(color);
    }
}
