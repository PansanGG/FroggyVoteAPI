package pansangg.froggyvoteapi;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pansangg.froggyvoteapi.site.SitePart;
import pansangg.froggyvoteapi.utils.Config;

import java.nio.file.Paths;
import java.util.HashMap;

public final class Main extends JavaPlugin {

    public static HashMap<String, Object> conf;
    public static Main me;
    public static SitePart site;
    public static Economy economy = null;

    @Override
    public void onEnable() {
        me = this;

        saveResource("config.yml", false);
        conf = new Config(Paths.get(getDataFolder().getPath(), "config.yml").toFile(), new HashMap<>());

        site = new SitePart((String) conf.get("site_host"), (Integer) conf.get("site_port"));

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Плагин отключен в связи с отсутствием плагина Vault! / Plugin disabled due to lack of Vault plugin");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            economy = rsp.getProvider();
            getLogger().info("Vault успешно подключен! / Vault was successfully connected!");
        }
    }

    @Override
    public void onDisable() {
        site.server.stop(1);
    }
}
