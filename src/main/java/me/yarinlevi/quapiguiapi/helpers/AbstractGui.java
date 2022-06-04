package me.yarinlevi.quapiguiapi.helpers;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.quapiguiapi.QGuiLoader;
import me.yarinlevi.quapiguiapi.exceptions.GuiNoItemException;
import me.yarinlevi.quapiguiapi.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.quapiguiapi.types.GuiItem;
import me.yarinlevi.quapiguiapi.types.Page;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author YarinQuapi
 */
public abstract class AbstractGui implements Listener, IGui {
    @Getter private Inventory inventory;
    @Getter @Setter private InventoryType inventoryType = InventoryType.CHEST;
    @Getter @Setter private String title;
    @Getter @Setter private int slots;
    @Getter private int maxPages;
    @Getter private int currentPage = 1;
    @Getter private final Map<Integer, Page> pages = new HashMap<>();
    @Getter private final LinkedHashMap<Integer, ItemStack> gitems = new LinkedHashMap<>();
    @Getter private final LinkedHashMap<Integer, GuiItem> items = new LinkedHashMap<>();
    private final int[] lockedSlots = new int[]{ slots-1, slots-2, slots-3, slots-4, slots-5, slots-6, slots-7, slots-8, slots-9 };

    public abstract void run(Player player);

    /**
     *
     * @return the inventory created.
     * @throws GuiNoItemException if the item list for the gui is empty
     */
    public Inventory initializeInventory() throws GuiNoItemException {
        inventory = Bukkit.createInventory(null, slots, title);

        if (items.size() == 0) {
            throw new GuiNoItemException();
        }

        this.initializeSlots();

        return inventory;
    }

    public void openPage(Player player, int page) {
        this.pages.get(page).constructPage(player, inventory);
    }

    public void openPage(Player player, int page, List<GuiItem> items) {
        this.pages.get(page).constructPage(player, inventory, items);
    }

    /**
     * Moves player to the next gui page
     * @param player the player
     * @return true if successful, false if player was on last page
     */
    public boolean nextPage(Player player) {
        if (currentPage < maxPages) {
            currentPage++;

            this.openPage(player, currentPage);
            return true;
        } else {
            player.closeInventory();
            return false;
        }
    }

    /**
     * Moves player to the previous page in the gui
     * @param player
     */
    public void previousPage(Player player) {
        if (currentPage <= maxPages) {
            currentPage--;

            this.openPage(player, currentPage);
        }
    }

    /**
     * Sorts items into different pages
     */
    public void initializeSlots() {
        int size = items.size() + (lockedSlots.length * (items.size() / slots)); // total size of the inventories combined
        this.maxPages = (int) Math.ceil((double) size / (slots - lockedSlots.length)); // ceil to round up max pages

        Page page = new Page(1, slots, lockedSlots, null, this.maxPages != 1); // create first page


        Stream<Map.Entry<Integer, GuiItem>> itemStream = items.entrySet().stream().sorted(Map.Entry.comparingByKey());
        int j = 0; // item slot
        for (int i = 0; i < size; i++) {

            if (j < (slots - lockedSlots.length)) { // is in locked slots?
                GuiItem item = itemStream.skip(i).findFirst().get().getValue(); // gets the item and the slot for the specified item

                page.addItem(item.slot() < this.slots-lockedSlots.length ? item.slot() : j, item.item()); // add item to page
                j++; // increase slot

                if (i == size-1) { // is last item?
                    pages.put(page.getId(), page); // add page to pages
                }
            } else {
                j = 0; // reset slot to 0
                pages.put(page.getId(), page); // add page to pages

                page = new Page(page.getId() + 1, slots, lockedSlots, null, this.maxPages != page.getId() + 1); // create new page with an incremented id
            }
        }
    }

    /**
     * Will self-register the class with the provided key
     * @param key to register, this key will be used to open the gui later.
     */
    public void register(String key) {
        QGuiLoader.getInstance().getApi().registerGui(key, this.getClass());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(final InventoryMoveItemEvent e) {
        if (e.getInitiator() == inventory) {
            e.setCancelled(true);
        }
    }
}
