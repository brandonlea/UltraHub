package dev.sha256.ultrahub;

import cl.bgmp.minecraft.util.commands.exceptions.*;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import dev.sha256.ultrahub.action.ActionManager;
import dev.sha256.ultrahub.command.CommandManager;
import dev.sha256.ultrahub.config.ConfigManager;
import dev.sha256.ultrahub.config.Messages;
import dev.sha256.ultrahub.cooldown.CooldownManager;
import dev.sha256.ultrahub.hook.HooksManager;
import dev.sha256.ultrahub.menu.MenuManager;
import dev.sha256.ultrahub.module.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Ultrahub extends JavaPlugin {

    private ConfigManager configManager;
    private HooksManager hooksManager;

    private ActionManager actionManager;
    private ModuleManager moduleManager;

    private CooldownManager cooldownManager;

    private CommandManager commandManager;

    private MenuManager menuManager;
    @Override
    public void onEnable() {

        getLogger().log(Level.INFO, " _____ _ _           _____     _   ");
        getLogger().log(Level.INFO, "|  |  | | |_ ___ ___|  |  |_ _| |_");
        getLogger().log(Level.INFO, "|  |  | |  _|  _| .'|     | | | . |");
        getLogger().log(Level.INFO, "|_____|_|_| |_| |__,|__|__|___|___|");
        getLogger().log(Level.INFO, "");
        getLogger().log(Level.INFO, "Version: " + getDescription().getVersion());
        getLogger().log(Level.INFO, "Author: SHA256_");
        getLogger().log(Level.INFO, "");

        MinecraftVersion.disableUpdateCheck();


        // Check plugin hooks
        hooksManager = new HooksManager(this);

        // Load config files
        configManager = new ConfigManager();
        configManager.loadFiles(this);

        if (!getServer().getPluginManager().isPluginEnabled(this)) return;

        // Register commands
        commandManager = new CommandManager(this);
        commandManager.reload();

        // Cooldown System
        cooldownManager = new CooldownManager();

        // Menu manager
        menuManager = new MenuManager();
        if(!hooksManager.isHookEnabled("HEAD_DATABASE"))  menuManager.onEnable(this);

        // Core plugin Modules
        moduleManager = new ModuleManager();
        moduleManager.loadModules(this);

        //Action System
        actionManager = new ActionManager(this);

        getLogger().info("Loaded " + getCommandManager().getCustomCommands().size() + " custom commands.");


        // Register BungeeCord channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
        moduleManager.unloadModules();
        menuManager.onDisable();
        configManager.saveFiles();
    }

    public void reload() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        configManager.reloadFiles();
        menuManager.onDisable();
        menuManager.onEnable(this);
        getCommandManager().reload();
        moduleManager.loadModules(this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public HooksManager getHooksManager() {
        return hooksManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            getCommandManager().execute(cmd.getName(), args, sender);
        } catch (CommandPermissionsException e) {
            Messages.NO_PERMISSION.send(sender);
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            //sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + "Usage: " + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An internal error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
