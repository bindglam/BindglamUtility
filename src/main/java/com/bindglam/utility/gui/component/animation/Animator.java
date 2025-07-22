package com.bindglam.utility.gui.component.animation;

import org.jetbrains.annotations.Nullable;

public class Animator {
    private Animation animation;

    private double time = 0.0;

    public Animator() {
    }

    public void update() {
        if(animation == null) return;

        if(time < animation.getLength()) {
            time = Math.min(time + (1.0 / 20.0), animation.getLength());
        } else {
            animation.onFinished().run();

            time = 0.0;
            animation = null;
        }
    }

    public int getOffset() {
        if(animation == null)
            return 0;
        return animation.getTimeline().getOffsetFrame(time);
    }

    public @Nullable String getGlyph() {
        if(animation == null)
            return null;
        return animation.getTimeline().getGlyphFrame(time);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void playAnimation(Animation animation) {
        this.animation = animation;

        this.time = 0.0;
    }

    public double getTime() {
        return time;
    }
}
