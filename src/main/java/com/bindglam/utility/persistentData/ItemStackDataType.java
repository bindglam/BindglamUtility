package com.bindglam.utility.persistentData;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(ItemStack complex, @NotNull PersistentDataAdapterContext context) {
        return complex.serializeAsBytes();
    }

    @NotNull
    @Override
    public ItemStack fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return ItemStack.deserializeBytes(primitive);
    }
}
