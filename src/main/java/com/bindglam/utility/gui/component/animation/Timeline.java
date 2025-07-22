package com.bindglam.utility.gui.component.animation;

import com.bindglam.utility.gui.component.animation.keyframe.AbstractKeyframe;
import com.bindglam.utility.gui.component.animation.keyframe.GlyphKeyframe;
import com.bindglam.utility.gui.component.animation.keyframe.KeyframeType;
import com.bindglam.utility.gui.component.animation.keyframe.OffsetKeyframe;
import com.bindglam.utility.math.GeoMath;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class Timeline {
    private final TreeMap<Double, OffsetKeyframe> offset = new TreeMap<>();
    private final TreeMap<Double, GlyphKeyframe> glyph = new TreeMap<>();

    private void addOffsetFrame(double frame, int value, KeyframeType type) {
        offset.put(frame, new OffsetKeyframe(type, value));
    }

    private void addGlyphFrame(double frame, String base, int glyphFrame, KeyframeType type) {
        glyph.put(frame, new GlyphKeyframe(type, base, glyphFrame));
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

        public Timeline build() {
            return timeline;
        }
    }
}
