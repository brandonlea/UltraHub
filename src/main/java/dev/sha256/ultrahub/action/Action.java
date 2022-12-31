package dev.sha256.ultrahub.action;

import dev.sha256.ultrahub.Ultrahub;
import org.bukkit.entity.Player;

public interface Action {

    String getIdentifier();

    void execute(Ultrahub plugin, Player player, String data);

}
