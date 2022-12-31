package dev.sha256.ultrahub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import dev.sha256.ultrahub.Permissions;
import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.config.Messages;
import dev.sha256.ultrahub.menu.AbstractMenu;
import dev.sha256.ultrahub.module.ModuleType;
import dev.sha256.ultrahub.module.modules.visual.scoreboard.ScoreboardManager;
import dev.sha256.ultrahub.utils.TextUtil;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class UltraHubCommand {

    private Ultrahub plugin;

    public UltraHubCommand(Ultrahub plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"ultrahub", "uhub"},
            desc = "View plugin infomation"
    )
    public void main(final CommandContext args, final CommandSender sender) throws CommandException {
        PluginDescriptionFile pdfFile = plugin.getDescription();

        if(args.argsLength() == 0 || args.getString(0).equalsIgnoreCase("help")) {
            if(!sender.hasPermission(Permissions.COMMAND_ULTRAHUB_HELP.getPermission())) {
                sender.sendMessage(TextUtil.color("&8&l> &7Server is running &4UltraHub &ev" + pdfFile.getVersion() + " &7By &6Sha256_"));
                return;
            }

            sender.sendMessage("");
            sender.sendMessage(TextUtil.color("&4UltraHub " + "&fv" + plugin.getDescription().getVersion()));
            sender.sendMessage(TextUtil.color("&7Author: &fSha256_"));
            sender.sendMessage("");
            sender.sendMessage(TextUtil.color(" &d/ultrahub scoreboard &8- &7&oToggle the scoreboard"));
            sender.sendMessage(TextUtil.color(" &d/ultrahub open <menu> &8- &7&oOpen a custom menu"));
            sender.sendMessage("");
            sender.sendMessage(TextUtil.color("  &d/vanish &8- &7&oToggle vanish mode"));
            sender.sendMessage(TextUtil.color("  &d/fly &8- &7&oToggle flight mode"));
            sender.sendMessage(TextUtil.color("  &d/hub &8- &7&oSet the spawn location"));
            sender.sendMessage(TextUtil.color("  &d/hub &8- &7&oTeleport to the spawn location"));
            sender.sendMessage(TextUtil.color("  &d/gamemode <gamemode> &8- &7&oSet your gamemode"));
            sender.sendMessage(TextUtil.color("  &d/gmc &8- &7&oGo into creative mode"));
            sender.sendMessage(TextUtil.color("  &d/gms &8- &7&oGo into survival mode"));
            sender.sendMessage(TextUtil.color("  &d/gma &8- &7&oGo into adventure mode"));
            sender.sendMessage(TextUtil.color("  &d/gmsp &8- &7&oGo into spectator mode"));
            sender.sendMessage(TextUtil.color("  &d/clearchat &8- &7&oClear global chat"));
            sender.sendMessage(TextUtil.color("  &d/lockchat &8- &7&oLock/unlock global chat"));
            sender.sendMessage("");
            return;
        } else if(args.getString(0).equalsIgnoreCase("reload")) {
            if(!sender.hasPermission(Permissions.COMMAND_ULTRAHUB_RELOAD.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            long start = System.currentTimeMillis();
            plugin.reload();
            Messages.CONFIG_RELOAD.send(sender, "%time%", String.valueOf(System.currentTimeMillis() - start));
        } else if(args.getString(0).equalsIgnoreCase("open")) {
            if(!(sender instanceof Player)) throw new CommandException("Console cannot open menus");

            if(!sender.hasPermission(Permissions.COMMAND_OPEN_MENUS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if(args.argsLength() == 1) {
                sender.sendMessage(TextUtil.color("&cUsage: /ultahub open <menu>"));
                return;
            }

            AbstractMenu menu = plugin.getMenuManager().getMenu(args.getString(1));
            if(menu == null) {
                sender.sendMessage(TextUtil.color("&c" + args.getString(1) + " is not a valid menu ID"));
                return;
            }
            menu.openMenu((Player) sender);
        } else if (args.getString(0).equalsIgnoreCase("scoreboard")) {

            if (!(sender instanceof Player)) throw new CommandException("Console cannot toggle the scoreboard");

            if (!sender.hasPermission(Permissions.COMMAND_SCOREBOARD_TOGGLE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if (!plugin.getModuleManager().isEnabled(ModuleType.SCOREBOARD)) {
                sender.sendMessage(TextUtil.color("&cThe scoreboard module is not enabled in the configuration."));
                return;
            }

            Player player = (Player) sender;
            ScoreboardManager scoreboardManager = ((ScoreboardManager) plugin.getModuleManager().getModule(ModuleType.SCOREBOARD));

            if (scoreboardManager.hasScore(player.getUniqueId())) {
                scoreboardManager.removeScoreboard(player);
                Messages.SCOREBOARD_TOGGLE.send(player, "%value%", "disabled");
            } else {
                scoreboardManager.createScoreboard(player);
                Messages.SCOREBOARD_TOGGLE.send(player, "%value%", "enabled");
            }
        }
    }
}
