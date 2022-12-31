package dev.sha256.ultrahub.menu;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {


    public final ItemStack itemStack;
    public final List<ClickAction> clickActions;

    public MenuItem(final ItemStack itemStack) {
        this.clickActions = new ArrayList<>();
        this.itemStack = itemStack;
    }

    public MenuItem addClickAction(final ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return clickActions;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
