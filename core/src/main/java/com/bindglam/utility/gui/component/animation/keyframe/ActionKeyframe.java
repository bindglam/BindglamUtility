package com.bindglam.utility.gui.component.animation.keyframe;

import java.util.UUID;

public class ActionKeyframe extends AbstractKeyframe<ActionKeyframe.Action> {
    public ActionKeyframe(Runnable action) {
        super(KeyframeType.STEP, new Action(action));
    }

    public static class Action {
        private final Runnable runnable;

        private final UUID uuid = UUID.randomUUID();

        public Action(Runnable runnable) {
            this.runnable = runnable;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public UUID getUniqueId() {
            return uuid;
        }
    }
}
