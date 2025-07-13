package com.bindglam.utility.guis;

import com.bindglam.utility.BindglamUtility;
import com.bindglam.utility.texts.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
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

import java.util.HashMap;

/**
 * 쉽고 빠르게 인벤토리 Gui를 생성하는 클래스
 */
public abstract class GuiBase implements InventoryHolder, Listener {
    private Inventory inv;

    private final Component title;
    private final HashMap<String, UIComponent> uiComponents = new HashMap<>();

    protected int taskID = -1;

    private boolean isUpdating = false;

    public GuiBase(int size, Component title, int updateTick){
        this.inv = Bukkit.createInventory(this, size, title);
        this.title = title;

        if(updateTick > 0) {
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(BindglamUtility.getInstance(), this::onTick, 0L, updateTick);
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

    public void onClose(InventoryCloseEvent event){
    }

    public void onTick(){
    }

    @EventHandler
    public void onOpenEvent(InventoryOpenEvent event){
        Inventory inventory = event.getView().getTopInventory();
        if(inventory.getHolder(false) != this || isUpdating) return;

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
    public void onCloseEvent(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getView().getTopInventory();
        if(inventory.getHolder(false) != this || isUpdating) return;

        onClose(event);

        if(taskID != -1) Bukkit.getScheduler().cancelTask(taskID);
        Bukkit.getScheduler().runTaskLater(BindglamUtility.getInstance(), () -> ((Player) event.getPlayer()).updateInventory(), 1L);
        HandlerList.unregisterAll(this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public HashMap<String, UIComponent> getUIComponents() {
        return uiComponents;
    }

    public void addUIComponent(UIComponent uiComponent){
        uiComponent.setParent(this);
        uiComponents.put(uiComponent.getID(), uiComponent);
        updateUIComponent();
    }

    public void updateUIComponent(){
        isUpdating = true;

        Component builtTitle = ComponentUtil.copyComponent(title);
        for(UIComponent component : uiComponents.values()){
            builtTitle = builtTitle.append(component.build());
        }

        Inventory newInventory = Bukkit.createInventory(this, inv.getSize(), builtTitle);
        newInventory.setContents(inv.getContents());
        for (int i = inv.getViewers().size() - 1; i >= 0; i--) {
            HumanEntity player = inv.getViewers().get(i);
            //player.getOpenInventory().setTitle(LegacyComponentSerializer.legacySection().serialize(builtTitle));
            player.openInventory(newInventory);
        }
        inv = newInventory;

        isUpdating = false;
    }
}
