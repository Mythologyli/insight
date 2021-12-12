package cc.akashic.insight;

import cc.akashic.insight.command.CommandInsight;
import cc.akashic.insight.command.CommandShareItems;
import cc.akashic.insight.command.CommandXray;
import cc.akashic.insight.utils.ShowItems;
import me.pikamug.localelib.LocaleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Insight extends JavaPlugin {
    public static Insight instance;

    private static LocaleManager localeManager;

    @Override
    public void onEnable() {
        instance = this;

        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        localeManager = new LocaleManager();

        this.getCommand("insight").setExecutor(new CommandInsight());
        this.getCommand("xray").setExecutor(new CommandXray());
        this.getCommand("shareitems").setExecutor(new CommandShareItems());

        getServer().getPluginManager().registerEvents(new EventBroadcastListener(), this);
        getServer().getPluginManager().registerEvents(new ShowItems.InventoryClickEventListener(), this);
    }

    @Override
    public void onDisable() {
        Log.info("Plugin stop.");
    }

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }
}
