package com.bindglam.utility.playerdata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VariableParser<T> {
    @Nullable Object parseToJSON(@NotNull Object obj);

    @Nullable T parseFromJSON(@NotNull Object json);

    @NotNull Class<T> getTypeClass();
}
