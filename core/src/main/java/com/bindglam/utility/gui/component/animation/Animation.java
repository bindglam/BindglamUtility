package com.bindglam.utility.gui.component.animation;

public class Animation {
    private final Timeline timeline;

    private double length;

    private Runnable onFinished;

    private Animation(Timeline timeline) {
        this.timeline = timeline;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public double getLength() {
        return length;
    }

    private void setLength(double length) {
        this.length = length;
    }

    public Runnable onFinished() {
        return onFinished;
    }

    private void onFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public static class Builder {
        private final Animation animation;

        private Builder(Timeline timeline) {
            animation = new Animation(timeline);
        }

        public static Builder builder(Timeline timeline) {
            return new Builder(timeline);
        }

        public Builder length(double length) {
            animation.setLength(length);
            return this;
        }

        public Builder onFinished(Runnable action) {
            animation.onFinished(action);
            return this;
        }

        public Animation build() {
            return animation;
        }
    }
}
