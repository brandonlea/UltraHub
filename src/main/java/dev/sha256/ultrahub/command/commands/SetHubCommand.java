package dev.sha256.ultrahub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import dev.sha256.ultrahub.Permissions;
import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.config.Messages;
import dev.sha256.ultrahub.module.ModuleType;
import dev.sha256.ultrahub.module.modules.world.LobbySpawn;
import dev.sha256.ultrahub.utils.TextUtil;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHubCommand {

    private final Ultrahub plugin;

    public SetHubCommand(Ultrahub plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"sethub"},
            desc = "Set the hub location"
    )
    public void sethub(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(Permissions.COMMAND_SET_LOBBY.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot set the spawn location.");
            return;
        }

        Player player = (Player) sender;
        if (plugin.getModuleManager().getDisabledWorlds().contains(player.getWorld().getName())) {
            sender.sendMessage(TextUtil.color("&cYou cannot set the hub location in a disabled world."));
            return;
        }

        LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
        lobbyModule.setLocation(player.getLocation());
        Messages.SET_LOBBY.send(sender);

    }
}
