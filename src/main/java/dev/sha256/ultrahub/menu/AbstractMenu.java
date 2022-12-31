package dev.sha256.ultrahub.menu;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractMenu implements Listener {

    private Ultrahub plugin;
    private boolean refreshedEnabled = false;
    private List<UUID> openMenus;

    public AbstractMenu(Ultrahub plugin) {
        this.plugin = plugin;
        openMenus = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void setMenuRefresh(long value) {
        if(value <= 0) return;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new MenuTask(this), 0L, value);
        refreshedEnabled = true;
    }

    public abstract void onEnable();

    protected abstract Inventory getMenu();

    protected Ultrahub getPlugin() {
        return plugin;
    }

    public Inventory refreshInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = getMenu().getItem(i);
            if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) continue;

            ItemStackBuilder newItem = new ItemStackBuilder(item.clone());
            if (item.getItemMeta().hasDisplayName()) newItem.withName(item.getItemMeta().getDisplayName(), player);
            if (item.getItemMeta().hasLore()) newItem.withLore(item.getItemMeta().getLore(), player);
            inventory.setItem(i, newItem.build());
        }
        return inventory;
    }

    public void openMenu(Player player) {
        if (getMenu() == null) return;

        player.openInventory(refreshInventory(player, getMenu()));
        if (refreshedEnabled && !openMenus.contains(player.getUniqueId())) {
            openMenus.add(player.getUniqueId());
        }
    }

    public List<UUID> getOpenMenus() {
        return openMenus;
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuBuilder && refreshedEnabled) {
            openMenus.remove(event.getPlayer().getUniqueId());
            //System.out.println("removed " + event.getPlayer().getName());
        }
    }
}
