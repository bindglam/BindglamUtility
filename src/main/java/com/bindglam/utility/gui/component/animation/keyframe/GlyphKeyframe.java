package com.bindglam.utility.gui.component.animation.keyframe;

public class GlyphKeyframe extends AbstractKeyframe<String> {
    private final String base;
    private final int frame;

    public GlyphKeyframe(KeyframeType type, String base, Integer frame) {
        super(type, null);

        this.base = base;
        this.frame = frame;
    }

    @Override
    public String getValue() {
        return base + "_" + frame;
    }

    public String getBase() {
        return base;
    }

    public int getFrame() {
        return frame;
    }
}
