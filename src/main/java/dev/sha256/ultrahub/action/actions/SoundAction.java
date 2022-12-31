package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import dev.sha256.ultrahub.utils.universal.XSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SoundAction implements Action {


    @Override
    public String getIdentifier() {
        return "SOUND";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        try {
            player.playSound(player.getLocation(), XSound.matchXSound(data).get().parseSound(), 1L, 1L);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[UltraHub Action] Invalid sound name: " + data.toUpperCase());
        }
    }
}
