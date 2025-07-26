package com.bindglam.utility.gui.component.animation.keyframe;

public abstract class AbstractKeyframe<T> {
    private final KeyframeType type;

    private final T value;

    public AbstractKeyframe(KeyframeType type, T value) {
        this.type = type;
        this.value = value;
    }

    public KeyframeType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}
