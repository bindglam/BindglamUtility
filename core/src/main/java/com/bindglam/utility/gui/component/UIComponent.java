package com.bindglam.utility.gui.component;

import com.bindglam.utility.gui.component.animation.Animation;
import com.bindglam.utility.gui.component.animation.Animator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public abstract class UIComponent {
    private final Animator animator = new Animator();

    public void playAnimation(Animation animation) {
        animator.playAnimation(animation);
    }

    public abstract Component build(TextColor color);

    public Component build() {
        return build(NamedTextColor.WHITE);
    }

    public Animator getAnimator() {
        return animator;
    }
}
