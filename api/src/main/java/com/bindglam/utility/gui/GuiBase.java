package com.bindglam.utility.gui;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.events.BindglamInventoryCloseEvent;
import com.bindglam.utility.gui.component.UIComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * 쉽고 빠르게 인벤토리 Gui를 생성하는 클래스
 */
public abstract class GuiBase implements InventoryHolder {
    private final Inventory inv;
    private final Component title;
    private int titleGlyphWidth = 0;

    private final int tickInterval;

    private final Map<String, UIComponent> uiComponents = new LinkedHashMap<>();
    private final Map<Integer, Map<String, Object>> itemData = new HashMap<>();

    private GuiRenderer renderer = null;

    private boolean isChanged = false;

    public GuiBase(int size, Component title, int updateTick){
        this.inv = Bukkit.createInventory(this, size, title);
        this.title = title;

        this.tickInterval = updateTick;
    }

    public GuiBase(int size, Component title){
        this(size, title, -1);
    }

    public GuiBase(int size, String glyphId, int offset, int updateTick){
        this(size, BindglamUtility.compatibility().getGlyph(glyphId, offset), updateTick);
        this.titleGlyphWidth = BindglamUtility.compatibility().getGlyphWidth(glyphId);
    }

    public GuiBase(int size, String glyphId, int offset){
        this(size, glyphId, offset, -1);
    }

    public void onOpen(InventoryOpenEvent event){
    }

    public void onClick(InventoryClickEvent event){
    }

    @Deprecated
    public void onClose(InventoryCloseEvent event){
    }

    public void onClose(BindglamInventoryCloseEvent event){
    }

    public void onTick(){
    }


    public void drawRectangleOutline(int x, int y, int width, int height, ItemStack itemStack) {
        for (int i = x; i <= x + width; i++) {
            inv.setItem(9 * y + i, itemStack);
            inv.setItem(9 * (y + height) + i, itemStack);
        }

        for (int i = y; i <= y + height; i++) {
            inv.setItem(x + 9 * i, itemStack);
            inv.setItem(x + 9 * i + width, itemStack);
        }
    }

    public void open(Player player, GuiRenderer renderer) {
        this.renderer = renderer;

        player.openInventory(inv);
        this.renderer.addViewer(player);
    }

    public void open(Player player) {
        open(player, BindglamUtility.guiRendererManager().createRenderer(this));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public Component getTitle() {
        return title;
    }

    public int getTitleGlyphWidth() {
        return titleGlyphWidth;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public @Unmodifiable Map<String, UIComponent> getUIComponents() {
        return Map.copyOf(uiComponents);
    }

    public void addUIComponent(String id, UIComponent uiComponent){
        uiComponents.put(id, uiComponent);

        isChanged = true;
    }

    public void removeUIComponent(String id) {
        uiComponents.remove(id);

        isChanged = true;
    }

    @Deprecated
    public @Nullable Object getItemData(int slot) {
        return getItemData(slot, "");
    }

    public @Nullable Object getItemData(int slot, String id) {
        if(!itemData.containsKey(slot))
            return null;
        return itemData.get(slot).get(id);
    }

    @Deprecated
    public void setItemData(int slot, Object data) {
        setItemData(slot, "", data);
    }

    public void setItemData(int slot, String id, Object data) {
        if(!itemData.containsKey(slot))
            itemData.put(slot, new HashMap<>());
        itemData.get(slot).put(id, data);
    }

    @Deprecated
    public void removeItemData(int slot) {
        removeItemData(slot, "");
    }

    public void removeItemData(int slot, String id) {
        if(!itemData.containsKey(slot))
            return;
        itemData.get(slot).remove(id);
        if(itemData.get(slot).isEmpty())
            itemData.remove(slot);
    }

    public @Nullable GuiRenderer getRenderer() {
        return renderer;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
