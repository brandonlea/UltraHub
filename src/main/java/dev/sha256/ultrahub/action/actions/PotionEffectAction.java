package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import dev.sha256.ultrahub.utils.universal.XPotion;
import org.bukkit.entity.Player;

public class PotionEffectAction implements Action {

    @Override
    public String getIdentifier() {
        return "EFFECT";
    }


    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        String[] args = data.split(";");
        int duration = 1000000;

        if(args.length == 3) {
            duration = Integer.parseInt(args[2]);
        }
        player.addPotionEffect(XPotion.matchXPotion(args[0]).get().parsePotion(duration, Integer.parseInt(args[1]) - 1));
    }
}