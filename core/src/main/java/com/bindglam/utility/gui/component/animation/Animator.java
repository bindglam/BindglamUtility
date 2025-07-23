package com.bindglam.utility.gui.component.animation;

import com.bindglam.utility.gui.component.animation.keyframe.ActionKeyframe;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Animator {
    private Animation animation;

    private double time = 0.0;

    private final Set<UUID> proceedActions = new HashSet<>();

    public Animator() {
    }

    public void update() {
        if(animation == null) return;

        if(time < animation.getLength()) {
            time = Math.min(time + (1.0 / 20.0), animation.getLength());

            ActionKeyframe.Action action = getAction();
            if(action != null && !proceedActions.contains(action.getUniqueId())) {
                action.getRunnable().run();
                proceedActions.add(action.getUniqueId());
            }
        } else {
            animation.onFinished().run();

            time = 0.0;
            animation = null;
            proceedActions.clear();
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

    public @Nullable TextColor getColor() {
        if(animation == null)
            return null;
        return animation.getTimeline().getColorFrame(time);
    }

    public @Nullable ActionKeyframe.Action getAction() {
        if(animation == null)
            return null;
        return animation.getTimeline().getActionFrame(time);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void playAnimation(Animation animation) {
        this.animation = animation;

        this.time = 0.0;
        this.proceedActions.clear();
    }

    public double getTime() {
        return time;
    }
}
