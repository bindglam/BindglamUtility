package com.bindglam.utility.manager;

import com.bindglam.utility.playerdata.VariableParser;
import org.jetbrains.annotations.Nullable;

public interface VariableParserManager {
    <T> void register(Class<T> clazz, VariableParser<T> parser);

    @Nullable Object parseToJSON(@Nullable Object object);

    @Nullable Object parseFromJSON(@Nullable Object json);
}
