package com.bindglam.utility.gui.component.animation;

import com.bindglam.utility.gui.component.animation.keyframe.*;
import com.bindglam.utility.math.GeoMath;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class Timeline {
    private final TreeMap<Double, OffsetKeyframe> offset = new TreeMap<>();
    private final TreeMap<Double, GlyphKeyframe> glyph = new TreeMap<>();
    private final TreeMap<Double, ColorKeyframe> color = new TreeMap<>();
    private final TreeMap<Double, ActionKeyframe> action = new TreeMap<>();

    private void addOffsetFrame(double frame, int value, KeyframeType type) {
        offset.put(frame, new OffsetKeyframe(type, value));
    }

    private void addGlyphFrame(double frame, String base, int glyphFrame, KeyframeType type) {
        glyph.put(frame, new GlyphKeyframe(type, base, glyphFrame));
    }

    private void addColorFrame(double frame, TextColor textColor, KeyframeType type) {
        color.put(frame, new ColorKeyframe(type, textColor));
    }

    private void addActionFrame(double frame, Runnable runnable) {
        action.put(frame, new ActionKeyframe(runnable));
    }

    public int getOffsetFrame(double time) {
        if (offset.isEmpty())
            return 0;
        if (offset.containsKey(time))
            return offset.get(time).getValue();

        double nextTime = getHigherKey(offset, time);
        double lastTime = getLowerKey(offset, time);
        if(nextTime == lastTime)
            return offset.get(lastTime).getValue();

        double t = (time - lastTime) / (nextTime - lastTime);

        OffsetKeyframe nextValue = offset.get(nextTime);
        OffsetKeyframe lastValue = offset.get(lastTime);

        return switch (getType(lastValue, nextValue)) {
            case LINEAR -> (int) GeoMath.lerp(lastValue.getValue(), nextValue.getValue(), t);
            case SMOOTH -> {
                double nextControlTime = getHigherKey(offset, nextTime);
                double lastControlTime = getLowerKey(offset, lastTime);
                OffsetKeyframe nextControlOffset = offset.get(nextControlTime);
                OffsetKeyframe lastControlOffset = offset.get(lastControlTime);
                yield (int) GeoMath.smoothLerp(lastControlOffset.getValue(), lastValue.getValue(), nextValue.getValue(), nextControlOffset.getValue(), t);
            }
            case STEP -> lastValue.getValue();
        };
    }

    public @Nullable String getGlyphFrame(double time) {
        if (glyph.isEmpty())
            return null;
        if (glyph.containsKey(time))
            return glyph.get(time).getValue();

        double nextTime = getHigherKey(glyph, time);
        double lastTime = getLowerKey(glyph, time);
        if(nextTime == lastTime)
            return glyph.get(lastTime).getValue();

        double t = (time - lastTime) / (nextTime - lastTime);

        GlyphKeyframe nextGlyph = glyph.get(nextTime);
        GlyphKeyframe lastGlyph = glyph.get(lastTime);

        return switch (getType(lastGlyph, nextGlyph)) {
            case LINEAR -> {
                int frame = (int) GeoMath.lerp(lastGlyph.getFrame(), nextGlyph.getFrame(), t);

                yield lastGlyph.getBase() + "_" + frame;
            }
            case SMOOTH -> {
                double nextControlTime = getHigherKey(glyph, nextTime);
                double lastControlTime = getLowerKey(glyph, lastTime);
                GlyphKeyframe nextControlFrame = glyph.get(nextControlTime);
                GlyphKeyframe lastControlFrame = glyph.get(lastControlTime);
                int frame = (int) GeoMath.smoothLerp(lastControlFrame.getFrame(), lastGlyph.getFrame(), nextGlyph.getFrame(), nextControlFrame.getFrame(), t);

                yield lastGlyph.getBase() + "_" + frame;
            }
            case STEP -> lastGlyph.getValue();
        };
    }

    public @Nullable TextColor getColorFrame(double time) {
        if (color.isEmpty())
            return null;
        if (color.containsKey(time))
            return color.get(time).getValue();

        double nextTime = getHigherKey(color, time);
        double lastTime = getLowerKey(color, time);
        if(nextTime == lastTime)
            return color.get(lastTime).getValue();

        double t = (time - lastTime) / (nextTime - lastTime);

        ColorKeyframe nextColor = color.get(nextTime);
        ColorKeyframe lastColor = color.get(lastTime);

        return switch (getType(lastColor, nextColor)) {
            case LINEAR -> GeoMath.lerp(lastColor.getValue(), nextColor.getValue(), t);
            case SMOOTH -> {
                double nextControlTime = getHigherKey(color, nextTime);
                double lastControlTime = getLowerKey(color, lastTime);
                ColorKeyframe nextControlFrame = color.get(nextControlTime);
                ColorKeyframe lastControlFrame = color.get(lastControlTime);

                yield GeoMath.smoothLerp(lastControlFrame.getValue(), lastColor.getValue(), nextColor.getValue(), nextControlFrame.getValue(), t);
            }
            case STEP -> lastColor.getValue();
        };
    }

    public @Nullable ActionKeyframe.Action getActionFrame(double time) {
        if (action.isEmpty())
            return null;
        if (action.containsKey(time))
            return action.get(time).getValue();

        double nextTime = getHigherKey(action, time);
        double lastTime = getLowerKey(action, time);
        if(nextTime == lastTime)
            return action.get(lastTime).getValue();

        double t = (time - lastTime) / (nextTime - lastTime);

        ActionKeyframe nextAction = action.get(nextTime);
        ActionKeyframe lastAction = action.get(lastTime);

        return switch (getType(lastAction, nextAction)) {
            case LINEAR, SMOOTH, STEP -> lastAction.getValue();
        };
    }

    private double getHigherKey(TreeMap<Double, ?> map, double time) {
        Double high = map.higherKey(time);
        if (high == null)
            return map.lastKey();
        return high;
    }

    private double getLowerKey(TreeMap<Double, ?> map, double time) {
        Double low = map.lowerKey(time);
        if (low == null)
            return map.firstKey();
        return low;
    }

    private KeyframeType getType(AbstractKeyframe<?> last, AbstractKeyframe<?> next) {
        if (last.getType() == KeyframeType.STEP)
            return KeyframeType.STEP;

        if (last.getType() == KeyframeType.SMOOTH || next.getType() == KeyframeType.SMOOTH)
            return KeyframeType.SMOOTH;

        return KeyframeType.LINEAR;
    }

    public static class Builder {
        private final Timeline timeline = new Timeline();

        private Builder() {
            timeline.addActionFrame(0.0, () -> {});
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder keyframe(double frame, int value, KeyframeType type) {
            timeline.addOffsetFrame(frame, value, type);
            return this;
        }

        public Builder keyframe(double frame, String base, int glyphFrame, KeyframeType type) {
            timeline.addGlyphFrame(frame, base, glyphFrame, type);
            return this;
        }

        public Builder keyframe(double frame, TextColor color, KeyframeType type) {
            timeline.addColorFrame(frame, color, type);
            return this;
        }

        public Builder keyframe(double frame, Runnable action) {
            timeline.addActionFrame(frame, action);
            return this;
        }

        public Timeline build() {
            return timeline;
        }
    }
}
