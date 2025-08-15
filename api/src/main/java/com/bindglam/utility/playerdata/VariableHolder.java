package com.bindglam.utility.playerdata;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface VariableHolder {
    <T> @Nullable T getVariable(@NotNull NamespacedKey key);

    @Deprecated
    <T> @Nullable T getVariable(@NotNull String name);

    @Nullable String getString(@NotNull NamespacedKey key);

    @Nullable Number getNumber(@NotNull NamespacedKey key);

    float getFloat(@NotNull NamespacedKey key);

    double getDouble(@NotNull NamespacedKey key);

    int getInt(@NotNull NamespacedKey key);

    long getLong(@NotNull NamespacedKey key);

    boolean getBoolean(@NotNull NamespacedKey key);

    void setVariable(@NotNull NamespacedKey key, @Nullable Object value);

    @Deprecated
    void setVariable(@NotNull String name, @Nullable Object value);

    boolean hasVariable(@NotNull NamespacedKey key);

    @Deprecated
    boolean hasVariable(@NotNull String name);

    void removeVariable(@NotNull NamespacedKey key);

    @Deprecated
    void removeVariable(@NotNull String name);

    Map<NamespacedKey, Object> getVariables();
}
