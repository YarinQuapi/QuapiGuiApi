package me.yarinlevi.quapiguiapi.listeners;

import me.yarinlevi.quapiguiapi.QGuiAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author YarinQuapi
 */
public class InventoryCloseListener implements Listener {
    private final QGuiAPI api;

    public InventoryCloseListener(QGuiAPI api) {
        this.api = api;
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();

        if (api.getUnregisterNext().containsKey(p)) {
            HandlerList.unregisterAll(api.getUnregisterNext().get(p));

            api.getUnregisterNext().remove(p);
        }
    }
}
