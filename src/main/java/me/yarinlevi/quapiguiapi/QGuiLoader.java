package me.yarinlevi.quapiguiapi;

import lombok.Getter;
import me.yarinlevi.quapiguiapi.helpers.AbstractGui;
import me.yarinlevi.quapiguiapi.listeners.InventoryCloseListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author YarinQuapi
 **/
public class QGuiLoader {
    @Getter
    private static QGuiLoader instance;

    @Getter private static JavaPlugin loaderPluginInstance;
    @Getter private QGuiAPI api;

    protected QGuiLoader(JavaPlugin loaderPlugin, Class<? extends AbstractGui> def) {
        instance = this;
        loaderPluginInstance = loaderPlugin;

        api = new QGuiAPI(def);
        loaderPlugin.getServer().getPluginManager().registerEvents(new InventoryCloseListener(api), loaderPlugin);
    }

    /**
     * Initializes the API
     * @param loaderPlugin the plugin loading the api
     * @param def the class of the default gui
     * @return a QGuiAPI object with the API calls.
     */
    public static QGuiAPI initializeApi(JavaPlugin loaderPlugin, Class<? extends AbstractGui> def) {
        QGuiLoader loader = new QGuiLoader(loaderPlugin, def);
        return loader.api;
    }
}
