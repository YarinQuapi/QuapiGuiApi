package me.yarinlevi.quapiguiapi;

import lombok.Getter;
import me.yarinlevi.quapiguiapi.helpers.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class QGuiAPI {
    private final HashMap<String, Class<? extends AbstractGui>> guiList = new HashMap<>();

    /**
     * do not touch this, this is used to clear guis from ram once they are not used!
     */
    @Getter
    private final Map<Player, AbstractGui> unregisterNext = new HashMap<>();

    private final Class<? extends AbstractGui> defaultGui;

    public QGuiAPI(Class<? extends AbstractGui> defaultGui) {
        this.defaultGui = defaultGui;
    }

    public void openInventory(String key, Player player) {
        player.closeInventory();

        try {
            AbstractGui gui = guiList.getOrDefault(key, defaultGui).newInstance();

            QGuiLoader.getLoaderPluginInstance().getServer().getPluginManager().registerEvents(gui, QGuiLoader.getLoaderPluginInstance());

            gui.run(player);

            unregisterNext.put(player, gui);

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerGui(String key, Class<? extends AbstractGui> guiClass) {
        this.guiList.put(key, guiClass);
    }
}
