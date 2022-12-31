package dev.sha256.ultrahub.action.actions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;

public class BungeeAction implements Action {

    @Override
    public String getIdentifier() {
        return "BUNGEE";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(data);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
