package dev.sha256.ultrahub.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MenuBuilder implements InventoryHolder {

    private final Map<Integer, MenuItem> icons;
    private int size;
    private String title;


    public MenuBuilder(int size, String title) {
        this.icons = new HashMap<>();
        this.size = size;
        this.title = title;
    }

    public void setItem(int slot, MenuItem item) {
        icons.put(slot, item);
    }

    public MenuItem getIcon(final int slot) {
        return icons.get(slot);
    }

    public Map<Integer, MenuItem> getIcons() {
        return icons;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        if(size > 54) size = 54;
        else if(size < 9) size = 9;

        Inventory menu = Bukkit.createInventory(this, size, title);
        for(Map.Entry<Integer, MenuItem> entry : icons.entrySet()) {
            menu.setItem(entry.getKey(), entry.getValue().getItemStack());
        }

        return menu;
    }

}
