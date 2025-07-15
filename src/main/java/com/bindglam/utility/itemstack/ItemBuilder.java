package com.bindglam.utility.itemstack;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(@NotNull ItemType type) {
        this.itemStack = type.createItemStack();
    }

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder displayName(Component text) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(text);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(Component... texts) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(Arrays.stream(texts).toList());
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<Component> texts) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(texts);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public <P, C> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<P, C> type, C value) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        persistentDataContainer.set(key, type, value);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideTooltip() {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setHideTooltip(true);
        itemStack.setItemMeta(meta);
        return this;
    }

    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    public static ItemBuilder createHead(OfflinePlayer player){
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) headItem.getItemMeta();
        meta.setOwningPlayer(player);
        headItem.setItemMeta(meta);
        return new ItemBuilder(headItem);
    }
}
