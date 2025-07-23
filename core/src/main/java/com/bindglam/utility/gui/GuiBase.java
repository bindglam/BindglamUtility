package com.bindglam.utility.gui;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.events.BindglamInventoryCloseEvent;
import com.bindglam.utility.gui.component.UIComponent;
import com.bindglam.utility.text.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 쉽고 빠르게 인벤토리 Gui를 생성하는 클래스
 */
public abstract class GuiBase implements InventoryHolder, Listener {
    private final Inventory inv;
    private final Set<UUID> viewers = new HashSet<>();

    private final Component originalTitle;
    private Component title;

    private final Map<String, UIComponent> uiComponents = new HashMap<>();
    private final Map<Integer, Object> itemData = new HashMap<>();

    protected int taskID = -1;

    public GuiBase(int size, Component title, int updateTick){
        this.inv = Bukkit.createInventory(this, size, title);
        this.originalTitle = title;
        this.title = title;

        if(updateTick > 0) {
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(BindglamUtility.getInstance(), () -> {
                boolean shouldUpdateUI = false;

                for(UIComponent component : uiComponents.values()) {
                    if(component.getAnimator().getAnimation() != null) {
                        shouldUpdateUI = true;

                        component.getAnimator().update();
                    }
                }

                if(shouldUpdateUI)
                    updateUIComponent();

                onTick();
            }, 0L, updateTick);
        }

        Bukkit.getPluginManager().registerEvents(this, BindglamUtility.getInstance());
    }

    public GuiBase(int size, Component title){
        this(size, title, -1);
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

    @EventHandler
    public void onOpenEvent(InventoryOpenEvent event){
        Inventory inventory = event.getView().getTopInventory();
        if(inventory.getHolder(false) != this) return;

        onOpen(event);

        Bukkit.getScheduler().runTaskLater(BindglamUtility.getInstance(), this::updateUIComponent, 2L);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getView().getTopInventory();
        if(inventory.getHolder(false) != this) return;

        if((event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER && event.getRawSlot() == 81) || event.getHotbarButton() == 0){
            player.sendMessage(Component.text("메뉴에서는 주무기 슬롯의 아이템을 꺼낼 수 없습니다.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        onClick(event);
    }

    @EventHandler
    public void onCloseEvent(BindglamInventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getView().getTopInventory();
        if(inventory.getHolder(false) != this) return;

        onClose((InventoryCloseEvent) event);
        onClose(event);

        if(event.isCancelled())
            return;

        if(taskID != -1) Bukkit.getScheduler().cancelTask(taskID);
        Bukkit.getScheduler().runTaskLater(BindglamUtility.getInstance(), () -> ((Player) event.getPlayer()).updateInventory(), 1L);
        HandlerList.unregisterAll(this);
        viewers.remove(player.getUniqueId());
    }

    public void open(Player player) {
        player.openInventory(inv);
        BindglamUtility.guiRenderer().sendFakeInventory(player, inv, title);

        viewers.add(player.getUniqueId());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public Map<String, UIComponent> getUIComponents() {
        return uiComponents;
    }

    public void addUIComponent(String id, UIComponent uiComponent){
        uiComponents.put(id, uiComponent);
        updateUIComponent();
    }

    public void updateUIComponent(){
        title = ComponentUtil.copyComponent(originalTitle);
        for(UIComponent component : uiComponents.values()){
            title = title.append(component.build());
        }

        for(Player player : viewers.stream().map(Bukkit::getPlayer).toList()) {
            BindglamUtility.guiRenderer().sendFakeInventory(player, inv, title);
        }
    }

    public @Nullable Object getItemData(int slot) {
        return itemData.get(slot);
    }

    public void setItemData(int slot, Object data) {
        itemData.put(slot, data);
    }

    public void removeItemData(int slot) {
        itemData.remove(slot);
    }
}
