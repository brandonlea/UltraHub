package dev.sha256.ultrahub.menu.menus;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.menu.AbstractMenu;
import dev.sha256.ultrahub.menu.MenuBuilder;
import dev.sha256.ultrahub.menu.MenuItem;
import dev.sha256.ultrahub.utils.ItemStackBuilder;
import dev.sha256.ultrahub.utils.TextUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

public class CustomGUI extends AbstractMenu {

    private MenuBuilder menu;
    private FileConfiguration config;

    public CustomGUI(Ultrahub plugin, FileConfiguration config) {
        super(plugin);
        this.config = config;
    }

    @Override
    public void onEnable() {

        MenuBuilder menuBuilder = new MenuBuilder(config.getInt("slots"), TextUtil.color(config.getString("title")));



        if (config.contains("refresh") && config.getBoolean("refresh.enabled")) {
            setMenuRefresh(config.getLong("refresh.rate"));
        }

        for (String entry : config.getConfigurationSection("items").getKeys(false)) {

            try {
                ItemStackBuilder builder = ItemStackBuilder.getItemStack(config.getConfigurationSection("items." + entry));

                MenuItem menuItem;
                if (!config.contains("items." + entry + ".actions")) {
                    menuItem = new MenuItem(builder.build());
                } else {
                    menuItem = new MenuItem(builder.build()).addClickAction(p -> getPlugin().getActionManager().executeActions(p, config.getStringList("items." + entry + ".actions")));
                }

                if (config.contains("items." + entry + ".slots")) {
                    for (String slot : config.getStringList("items." + entry + ".slots")) {
                        menuBuilder.setItem(Integer.parseInt(slot), menuItem);
                    }
                } else if (config.contains("items." + entry + ".slot")) {
                    int slot = config.getInt("items." + entry + ".slot");
                    if (slot == -1) {
                        while (menuBuilder.getInventory().firstEmpty() != -1) {
                            menuBuilder.setItem(menuBuilder.getInventory().firstEmpty(), menuItem);
                        }
                    } else menuBuilder.setItem(slot, menuItem);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getPlugin().getLogger().warning("There was an error loading GUI item ID '" + entry + "', skipping..");
            }
        }

        menu = menuBuilder;

    }

    @Override
    protected Inventory getMenu() {
        return menu.getInventory();
    }

}
