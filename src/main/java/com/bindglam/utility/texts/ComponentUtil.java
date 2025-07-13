package com.bindglam.utility.texts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ComponentUtil {
    public static Component copyComponent(Component original) {
        if (original == null) {
            return null;
        }

        String json = GsonComponentSerializer.gson().serialize(original);

        return GsonComponentSerializer.gson().deserialize(json);
    }
}
