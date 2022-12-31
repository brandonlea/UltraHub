package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import dev.sha256.ultrahub.config.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetPrefixAction implements Action {
    @Override
    public String getIdentifier() {
        return "SETPREFIX";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        FileConfiguration config = plugin.getConfigManager().getFile(ConfigType.DATA).getConfig();
        config.set("Players." + player.getUniqueId() + ".chat.prefix", data);
        plugin.getConfigManager().saveFiles();
    }
}
