package dev.sha256.ultrahub.menu;

import dev.sha256.ultrahub.Ultrahub;
import dev.sha256.ultrahub.menu.menus.CustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private Ultrahub plugin;

    private Map<String, AbstractMenu> menus;

    private Player player;

    public MenuManager() {
        menus = new HashMap<>();
    }

    public void onEnable(Ultrahub plugin) {
        this.plugin = plugin;

        loadCustomMenus();

        menus.values().forEach(AbstractMenu::onEnable);

        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    private void loadCustomMenus() {

        File directory = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus");

        if (!directory.exists()) {
            directory.mkdir();
            File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus", "serverselector.yml");
            try (final InputStream inputStream = this.plugin.getResource("serverselector.yml")) {
                file.createNewFile();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                final OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Load all menu files
        File[] yamlFiles = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus").listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) return;

        for (File file : yamlFiles) {
            String name = file.getName().replace(".yml", "");
            if (menus.containsKey(name)) {
                plugin.getLogger().warning("Inventory with name '" + file.getName() + "' already exists, skipping duplicate..");
                continue;
            }

            CustomGUI customGUI;
            try {
                customGUI = new CustomGUI(plugin, YamlConfiguration.loadConfiguration(file));
            } catch (Exception e) {
                plugin.getLogger().severe("Could not load file '" + name + "' (YAML error).");
                e.printStackTrace();
                continue;
            }

            menus.put(name, customGUI);
            plugin.getLogger().info("Loaded custom menu '" + name + "'.");
        }
    }

    public void addMenu(String key, AbstractMenu inventory) {
        menus.put(key, inventory);
    }

    public Map<String, AbstractMenu> getMenus() {
        return menus;
    }

    public AbstractMenu getMenu(String key) {
        return menus.get(key);
    }

    public void onDisable() {
        menus.values().forEach(abstractInventory -> {
            for (UUID uuid : abstractInventory.getOpenMenus()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) player.closeInventory();
            }
            abstractInventory.getOpenMenus().clear();
        });
        menus.clear();
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
