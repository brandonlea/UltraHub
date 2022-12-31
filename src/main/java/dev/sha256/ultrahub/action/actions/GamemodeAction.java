package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeAction implements Action {

    @Override
    public String getIdentifier() {
        return "GAMEMODE";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        try {
            player.setGameMode(GameMode.valueOf(data.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().warning("[UltraHub Action] Invalid gamemode name: " + data.toUpperCase());
        }
    }
}