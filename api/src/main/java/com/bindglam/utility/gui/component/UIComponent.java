package com.bindglam.utility.gui.component;

import com.bindglam.utility.gui.component.animation.Animation;
import com.bindglam.utility.gui.component.animation.Animator;
import net.kyori.adventure.text.Component;

public abstract class UIComponent {
    private final Animator animator = new Animator();

    private int offset = 0;

    public void playAnimation(Animation animation) {
        animator.playAnimation(animation);
    }

    public abstract Component build(int offset);

    public abstract int width();

    public Animator getAnimator() {
        return animator;
    }

    public void setOffset(int value) {
        this.offset = value;
    }

    public int getOffset() {
        return offset;
    }
}
