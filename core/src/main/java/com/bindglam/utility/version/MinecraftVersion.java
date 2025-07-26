package com.bindglam.utility.version;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public final class MinecraftVersion implements Comparable<MinecraftVersion> {
    private static final Comparator<MinecraftVersion> COMPARATOR = Comparator.<MinecraftVersion, Integer>comparing((v) -> v.first).thenComparing((v) -> v.second).thenComparing((v) -> v.third);;

    public static final MinecraftVersion CURRENT_VERSION = new MinecraftVersion(Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf('-')));

    public static final MinecraftVersion V1_21_4 = new MinecraftVersion(1, 21, 4);
    public static final MinecraftVersion V1_21_8 = new MinecraftVersion(1, 21, 8);

    private final int first, second, third;

    public MinecraftVersion(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public MinecraftVersion(String[] ver) {
        if(ver.length > 0) this.first = Integer.parseInt(ver[0]);
        else this.first = 0;

        if(ver.length > 1) this.second = Integer.parseInt(ver[1]);
        else this.second = 0;

        if(ver.length > 2) this.third = Integer.parseInt(ver[2]);
        else this.third = 0;
    }

    public MinecraftVersion(String ver) {
        this(ver.split("\\."));
    }

    @Override
    public int compareTo(@NotNull MinecraftVersion o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        if(third == 0)
            return first + "." + second;
        else
            return first + "." + second + "." + third;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MinecraftVersion other))
            return false;
        return this.toString().equals(other.toString());
    }
}
