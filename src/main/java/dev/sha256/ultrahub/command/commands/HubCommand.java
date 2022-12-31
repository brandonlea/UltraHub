package dev.sha256.ultrahub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.module.ModuleType;
import dev.sha256.ultrahub.module.modules.world.LobbySpawn;
import dev.sha256.ultrahub.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand {

    private Ultrahub plugin;

    public HubCommand(Ultrahub plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"hub"},
            desc = "Teleport to the hub (if set)"
    )
    public void hub(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot teleport to spawn");
            return;
        }

        Location location = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY)).getLocation();
        if (location == null) {
            sender.sendMessage(TextUtil.color("&cThe spawn location has not been set &7(/sethub)&c."));
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> ((Player) sender).teleport(location), 3L);

    }
}
