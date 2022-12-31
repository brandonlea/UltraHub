package dev.sha256.ultrahub.module;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.config.ConfigType;
import dev.sha256.ultrahub.cooldown.CooldownManager;
import dev.sha256.ultrahub.cooldown.CooldownType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Module implements Listener {

    private Ultrahub plugin;
    private ModuleType moduleType;
    private List<String> disableWorlds;
    private CooldownManager cooldownManager;

    public Module(Ultrahub plugin, ModuleType moduleType) {
        this.plugin = plugin;
        this.moduleType = moduleType;
        this.cooldownManager = plugin.getCooldownManager();
        this.disableWorlds = new ArrayList<>();
    }

    public void setDisableWorlds(List<String> disableWorlds) {
        this.disableWorlds = disableWorlds;
    }

    public Ultrahub getPlugin() {
        return plugin;
    }

    public boolean inDisabledWorld(Location location) {
        return disableWorlds.contains(location.getWorld().getName());
    }

    public boolean inDisabledWorld(World world) {
        return disableWorlds.contains(world.getName());
    }

    public boolean tryCooldown(UUID uuid, CooldownType type, long delay) {
        return cooldownManager.tryCooldown(uuid, type, delay);
    }

    public String getCooldown(UUID uuid, CooldownType type) {
        return String.valueOf(cooldownManager.getCooldown(uuid, type) / 1000);
    }

    public FileConfiguration getConfig(ConfigType type) {
        return getPlugin().getConfigManager().getFile(type).getConfig();
    }

    public void executeActions(Player player, List<String> actions) {
        getPlugin().getActionManager().executeActions(player, actions);
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
