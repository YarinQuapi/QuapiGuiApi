package me.yarinlevi.quapiguiapi.helpers;

import me.yarinlevi.quapiguiapi.exceptions.GuiNoItemException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author YarinQuapi
 */
public interface IGui {
    void run(Player player);

    Inventory initializeInventory() throws GuiNoItemException;
}
