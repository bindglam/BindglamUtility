package com.bindglam.utility.gui.component.animation;

public class Animation {
    private final Timeline timeline;

    private double length;

    private Runnable onFinished;

    public Animation(Timeline timeline) {
        this.timeline = timeline;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Runnable onFinished() {
        return onFinished;
    }

    public void onFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
