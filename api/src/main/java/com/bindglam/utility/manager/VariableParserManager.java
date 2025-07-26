package com.bindglam.utility.manager;

import com.bindglam.utility.playerdata.VariableParser;
import org.jetbrains.annotations.Nullable;

public interface VariableParserManager {
    void register(VariableParser<?> parser);

    @Nullable Object parseToJSON(@Nullable Object object);

    @Nullable Object parseFromJSON(@Nullable Object json);
}
