package com.bindglam.utility.gui;

import com.bindglam.utility.BindglamUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * Gui에 가변성 이미지를 넣고 싶을 때 사용하는 클래스
 */
public class UIComponent {
    private final String id;

    private String glyphId;
    private int offsetX;
    private TextColor color = NamedTextColor.WHITE;

    private GuiBase parent;

    public UIComponent(String id, String glyphId){
        this.id = id;
        this.glyphId = glyphId;
    }

    public String getID(){
        return id;
    }

    public String getGlyphID() {
        return glyphId;
    }

    public void setFontImage(String glyphId) {
        this.glyphId = glyphId;

        if(parent != null)
            parent.updateUIComponent();
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;

        if(parent != null)
            parent.updateUIComponent();
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;

        if(parent != null)
            parent.updateUIComponent();
    }

    public void setParent(GuiBase parent) {
        this.parent = parent;
    }

    public Component build(){
        return BindglamUtility.getInstance().getCompatibility().getGlyph(glyphId, offsetX).color(color);
    }
}
