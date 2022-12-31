package dev.sha256.ultrahub.module.modules.hotbar;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.config.ConfigType;
import dev.sha256.ultrahub.module.Module;
import dev.sha256.ultrahub.module.ModuleType;
import dev.sha256.ultrahub.module.modules.hotbar.items.CustomItem;
import dev.sha256.ultrahub.module.modules.hotbar.items.Enderbutt;
import dev.sha256.ultrahub.module.modules.hotbar.items.PlayerHider;
import dev.sha256.ultrahub.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HotbarManager extends Module {

    private List<HotbarItem> hotbarItems;

    public HotbarManager(Ultrahub plugin) {
        super(plugin, ModuleType.HOTBAR_ITEMS);
    }

    @Override
    public void onEnable() {
        hotbarItems = new ArrayList<>();
        FileConfiguration config = getConfig(ConfigType.SETTINGS);

        if (config.getBoolean("custom_join_items.enabled")) {

            for (String entry : config.getConfigurationSection("custom_join_items.items").getKeys(false)) {
                ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("custom_join_items.items." + entry)).build();
                CustomItem customItem = new CustomItem(this, item, config.getInt("custom_join_items.items." + entry + ".slot"), entry);

                if (config.contains("custom_join_items.items." + entry + ".permission")) {
                    customItem.setPermission(config.getString("custom_join_items.items." + entry + ".permission"));
                }

                customItem.setConfigurationSection(config.getConfigurationSection("custom_join_items.items." + entry));
                customItem.setAllowMovement(config.getBoolean("custom_join_items.disable_inventory_movement"));
                registerHotbarItem(customItem);
            }

        }

        if (config.getBoolean("player_hider.enabled")) {
            ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("player_hider.not_hidden")).build();
            PlayerHider playerHider = new PlayerHider(this, item, config.getInt("player_hider.slot"), "PLAYER_HIDER");

            playerHider.setAllowMovement(config.getBoolean("player_hider.disable_inventory_movement"));

            registerHotbarItem(playerHider);
        }

        if(config.getBoolean("ender_butt.enabled")) {
            ItemStack item = ItemStackBuilder.getItemStack(config.getConfigurationSection("ender_butt.pearl")).build();
            Enderbutt enderbutt = new Enderbutt(this, item, config.getInt("ender_butt.slot"), "ENDER_BUTT");

            enderbutt.setAllowMovement(config.getBoolean("ender_butt.disable_inventory_movement"));

            registerHotbarItem(enderbutt);
        }

        giveItems();
    }

    @Override
    public void onDisable() {
        removeItems();
    }

    public List<HotbarItem> getHotbarItems() {
        return hotbarItems;
    }

    public void registerHotbarItem(HotbarItem hotbarItem) {
        getPlugin().getServer().getPluginManager().registerEvents(hotbarItem, getPlugin());
        hotbarItems.add(hotbarItem);
    }

    private void giveItems() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.giveItem(player)));
    }

    private void removeItems() {
        Bukkit.getOnlinePlayers().stream().filter(player -> !inDisabledWorld(player.getLocation())).forEach(player -> hotbarItems.forEach(hotbarItem -> hotbarItem.removeItem(player)));
    }

}