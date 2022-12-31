package dev.sha256.ultrahub.action;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.action.actions.*;
import dev.sha256.ultrahub.utils.PlaceholderUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private final Ultrahub plugin;
    private final Map<String, Action> actions;

    public ActionManager(Ultrahub plugin) {
        this.plugin = plugin;
        actions = new HashMap<>();
        load();
    }

    private void load() {
        registerActions(
                new SoundAction(),
                new BungeeAction(),
                new CloseMenuAction(),
                new MessageAction(),
                new MenuAction(),
                new PotionEffectAction(),
                new TitleAction(),
                new GamemodeAction(),
                new SetPrefixAction()
        );
    }

    public void registerActions(Action... actions) {
        Arrays.asList(actions).forEach(action -> this.actions.put(action.getIdentifier(), action));
    }

    public void executeActions(Player player, List<String> items) {
        items.forEach(item -> {
            String actionName = StringUtils.substringBetween(item, "[", "]");
            Action action = actionName == null ? null : actions.get(actionName.toUpperCase());

            if (action != null) {
                item = item.contains(" ") ? item.split(" ", 2)[1] : "";
                item = PlaceholderUtil.setPlaceholders(item, player);

                action.execute(plugin, player, item);
            }else{
                plugin.getLogger().warning("There was a problem attempting to process action: '" + item + "'");
            }
        });
    }
}
