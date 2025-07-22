package com.bindglam.utility.math;

public final class GeoMath {
    public static double lerp(double a, double b, double aT, double bT) {
        return aT * a + bT * b;
    }

    public static double lerp(double a, double b, double t) {
        return lerp(a, b, 1 - t, t);
    }

    public static double smoothLerp(double a, double b, double c, double d, double t) {
        double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
        t = (t2 - t1) * t + t1;

        double a1 = lerp(a, b, (t1 - t) / (t1 - t0), (t - t0) / (t1 - t0));
        double a2 = lerp(b, c, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
        double a3 = lerp(c, d, (t3 - t) / (t3 - t2), (t - t2) / (t3 - t2));

        double b1 = lerp(a1, a2, (t2 - t) / (t2 - t0), (t - t0) / (t2 - t0));
        double b2 = lerp(a2, a3, (t3 - t) / (t3 - t1), (t - t1) / (t3 - t1));

        return lerp(b1, b2, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
    }
}
