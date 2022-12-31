package dev.sha256.ultrahub.module.modules.hotbar.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.sha256.ultrahub.config.ConfigType;
import dev.sha256.ultrahub.config.Messages;
import dev.sha256.ultrahub.cooldown.CooldownType;
import dev.sha256.ultrahub.module.modules.hotbar.HotbarItem;
import dev.sha256.ultrahub.module.modules.hotbar.HotbarManager;
import dev.sha256.ultrahub.utils.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

public class Enderbutt extends HotbarItem {


    private ItemStack enderButt;

    private FileConfiguration config;



    public Enderbutt(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);
        config = getHotbarManager().getConfig(ConfigType.SETTINGS);
        NBTItem nbtItem = new NBTItem(ItemStackBuilder.getItemStack(config.getConfigurationSection("ender_butt.pearl")).build());
        enderButt = nbtItem.getItem();
    }

    @Override
    protected void onInteract(Player player) {

    }

    @EventHandler
    public void onPearlClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getItem() == null) return;
            if(event.getItem().getType() == Material.AIR) return;

            if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(enderButt.getItemMeta().getDisplayName())) {
                Player player = event.getPlayer();

                Location playerLocation = player.getLocation();
                Vector toLocation = playerLocation.getDirection();

                if(config.contains("ender_butt.multiplier")) {
                    player.setVelocity(toLocation.multiply(config.getInt("ender_butt.multiplier")));
                } else {
                    player.setVelocity(toLocation.multiply(2));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.ENDER_PEARL)
        {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();

        if(player.isInsideVehicle() && player.getVehicle().getType() == EntityType.ENDER_PEARL && !player.getVehicle().isDead()) {
            event.setCancelled(true);
            return;
        }

        event.getEntity().addPassenger(player);
    }

    @EventHandler
    public void onEntityDismound(final EntityDismountEvent event) {
        if(event.getEntityType() != EntityType.PLAYER || event.getDismounted().getType() != EntityType.ENDER_PEARL) {
            return;
        }

        Player player = (Player) event.getEntity();

        player.setItemInHand(enderButt);
        player.updateInventory();


    }
}
