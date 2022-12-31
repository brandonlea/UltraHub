package dev.sha256.ultrahub.action.actions;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.Action;
import dev.sha256.ultrahub.utils.TextUtil;
import org.bukkit.entity.Player;

public class MessageAction implements Action {

    @Override
    public String getIdentifier() {
        return "MESSAGE";
    }

    @Override
    public void execute(Ultrahub plugin, Player player, String data) {
        if(data.contains("<center>") && data.contains("</center>")) data = TextUtil.getCenteredMessage(data);
        player.sendMessage(TextUtil.color(data));
    }
}
