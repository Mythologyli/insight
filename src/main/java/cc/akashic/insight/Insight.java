package cc.akashic.insight;

import cc.akashic.insight.command.CommandInsight;
import cc.akashic.insight.command.CommandShareItems;
import cc.akashic.insight.command.CommandXray;
import cc.akashic.insight.utils.ItemsViewer;
import me.pikamug.localelib.LocaleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Insight extends JavaPlugin {
    public static Insight instance;

    private static LocaleManager localeManager;

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        localeManager = new LocaleManager();

        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        this.getCommand("insight").setExecutor(new CommandInsight());
        this.getCommand("xray").setExecutor(new CommandXray());
        this.getCommand("shareitems").setExecutor(new CommandShareItems());

        getServer().getPluginManager().registerEvents(new EventBroadcastListener(), this);
        getServer().getPluginManager().registerEvents(new ItemsViewer.InventoryClickEventListener(), this);
    }

    @Override
    public void onDisable() {
        Log.info("Plugin stop.");
    }
}
