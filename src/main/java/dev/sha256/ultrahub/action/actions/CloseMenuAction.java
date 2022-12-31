package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import org.bukkit.entity.Player;

public class CloseMenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "CLOSE";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        player.closeInventory();
    }
}
