package dev.sha256.ultrahub.menu;

import dev.sha256.ultrahub.Ultrahub;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private Ultrahub plugin = Ultrahub.getPlugin(Ultrahub.class);

    @EventHandler
    public void onMenuOpen(InventoryOpenEvent event) {
        if(event.getView().getTopInventory().getHolder() instanceof MenuBuilder) {

            if(event.getPlayer() instanceof Player) {
                MenuBuilder customHolder = (MenuBuilder) event.getView().getTopInventory().getHolder();
                plugin.getMenuManager().setPlayer(((Player) event.getPlayer()).getPlayer());
            }
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if(event.getView().getTopInventory().getHolder() instanceof MenuBuilder) {
            event.setCancelled(true);

            if(event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                ItemStack itemStack = event.getCurrentItem();

                if(itemStack == null || itemStack.getType() == Material.AIR) return;

                MenuBuilder customHolder = (MenuBuilder) event.getView().getTopInventory().getHolder();
                MenuItem item = customHolder.getIcon(event.getRawSlot());

                if(item == null) return;

                for(final ClickAction clickAction : item.getClickActions()) {
                    clickAction.execute(player);
                }
            }
        }
    }

}
