package dev.sha256.ultrahub.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MenuTask implements Runnable {


    private final AbstractMenu menu;

    MenuTask(AbstractMenu menu) {
        this.menu = menu;
    }

    @Override
    public void run() {
        for (UUID uuid : menu.getOpenMenus()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                menu.refreshInventory(player, player.getOpenInventory().getTopInventory());
            }
        }
    }
}
