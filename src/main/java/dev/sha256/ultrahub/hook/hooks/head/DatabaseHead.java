package dev.sha256.ultrahub.hook.hooks.head;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.hook.PluginHook;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DatabaseHead implements PluginHook, HeadHook, Listener {


    private Ultrahub plugin;
    private HeadDatabaseAPI api;

    @Override
    public void onEnable(Ultrahub plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        api = new HeadDatabaseAPI();
    }

    @Override
    public ItemStack getHead(String data) {
        return api.getItemHead(data);
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent event) {
        plugin.getMenuManager().onEnable(plugin);
    }
}
