package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import dev.sha256.ultrahub.menu.AbstractMenu;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "MENU";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        AbstractMenu menu = plugin.getMenuManager().getMenu(data);

        if(menu != null) {
            menu.openMenu(player);
        } else {
            plugin.getLogger().warning("[MENU] Action Failed: Menu '" + data + "' not found.");
        }
    }
}
