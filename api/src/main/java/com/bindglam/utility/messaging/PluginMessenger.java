package com.bindglam.utility.messaging;

import com.google.common.io.ByteArrayDataInput;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface PluginMessenger {
    void send(String cmd, List<String> args, Consumer<ByteArrayDataInput> consumer);

    default void sender(String cmd, String... args) {
        send(cmd, Arrays.stream(args).toList(), (in) -> {});
    }

    default void sender(String cmd, List<String> args) {
        send(cmd, args, (in) -> {});
    }
}
