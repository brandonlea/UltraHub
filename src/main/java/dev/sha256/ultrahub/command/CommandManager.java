package dev.sha256.ultrahub.command;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.minecraft.util.commands.CommandsManager;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.command.commands.*;
import dev.sha256.ultrahub.command.commands.gamemodes.*;
import dev.sha256.ultrahub.config.ConfigType;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final Ultrahub plugin;
    private final FileConfiguration config;

    private CommandsManager commands;
    private CommandsManagerRegistration commandRegistry;

    private final List<CustomCommand> customCommands;

    public CommandManager(Ultrahub plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager().getFile(ConfigType.COMMANDS).getConfig();
        this.customCommands = new ArrayList<>();
    }

    public void reload() {
        if (commandRegistry != null) commandRegistry.unregisterCommands();

        commands = new BukkitCommandsManager();
        commandRegistry = new CommandsManagerRegistration(plugin, commands);
        commands.setInjector(new SimpleInjector(plugin));

        commandRegistry.register(UltraHubCommand.class);

        for (String command : config.getConfigurationSection("commands").getKeys(false)) {
            if (!config.getBoolean("commands." + command + ".enabled")) continue;

            registerCommand(command, config.getStringList("commands." + command + ".aliases").toArray(new String[0]));
        }

        reloadCustomCommands();
    }

    public void execute(String cmd, String[] args, CommandSender sender) throws CommandException {
        commands.execute(cmd, args, sender, sender);
    }

    public void reloadCustomCommands() {
        if (!customCommands.isEmpty()) customCommands.clear();
        if (!config.isSet("custom_commands")) return;

        for (String entry : config.getConfigurationSection("custom_commands").getKeys(false)) {

            CustomCommand customCommand = new CustomCommand(entry, config.getStringList("custom_commands." + entry + ".actions"));

            if (config.contains("custom_commands." + entry + ".aliases")) {
                customCommand.addAliases(config.getStringList("custom_commands." + entry + ".aliases"));
            }

            if (config.contains("custom_commands." + entry + ".permission")) {
                customCommand.setPermission(config.getString("custom_commands." + entry + ".permission"));
            }

            customCommands.add(customCommand);
        }
    }


    private void registerCommand(String cmd, String[] aliases) {
        switch (cmd.toUpperCase()) {
            case "GAMEMODE":
                commandRegistry.register(GamemodeCommand.class, aliases);
                break;
            case "GMS":
                commandRegistry.register(SurvivalCommand.class, aliases);
                break;
            case "GMC":
                commandRegistry.register(CreativeCommand.class, aliases);
                break;
            case "GMA":
                commandRegistry.register(AdventureCommand.class, aliases);
                break;
            case "GMSP":
                commandRegistry.register(SpectatorCommand.class, aliases);
                break;
            case "CLEARCHAT":
                commandRegistry.register(ClearchatCommand.class, aliases);
                break;
            case "FLY":
                commandRegistry.register(FlyCommand.class, aliases);
                break;
            case "LOCKCHAT":
                commandRegistry.register(LockchatCommand.class, aliases);
                break;
            case "SETHUB":
                commandRegistry.register(SetHubCommand.class, aliases);
                break;
            case "HUB":
                commandRegistry.register(HubCommand.class, aliases);
                break;
            case "VANISH":
                commandRegistry.register(VanishCommand.class, aliases);
                break;
        }
    }

    public List<CustomCommand> getCustomCommands() {
        return customCommands;
    }
}
