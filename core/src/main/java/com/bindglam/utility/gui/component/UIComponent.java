package com.bindglam.utility.gui.component;

import com.bindglam.utility.gui.component.animation.Animation;
import com.bindglam.utility.gui.component.animation.Animator;
import net.kyori.adventure.text.Component;

public abstract class UIComponent {
    private final Animator animator = new Animator();

    public void playAnimation(Animation animation) {
        animator.playAnimation(animation);
    }

    public abstract Component build();

    public Animator getAnimator() {
        return animator;
    }
}
