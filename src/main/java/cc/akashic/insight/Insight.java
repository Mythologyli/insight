package cc.akashic.insight;

import cc.akashic.insight.command.CommandInsight;
import cc.akashic.insight.command.CommandShareItems;
import cc.akashic.insight.command.CommandTPSKeep;
import cc.akashic.insight.command.CommandXray;
import cc.akashic.insight.utils.ItemsViewer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Insight extends JavaPlugin {
    public static Insight instance;

    @Override
    public void onEnable() {
        instance = this;

        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        AFK.createAFKTeam();

        boolean isSparkExist = TPSKeeper.getSpark();
        TPSKeeper.getWorld();
        TPSKeeper.saveOriginMonsterSpawnLimit();
        TPSKeeper.disableTPSKeepMode();

        this.getCommand("insight").setExecutor(new CommandInsight());
        this.getCommand("xray").setExecutor(new CommandXray());
        this.getCommand("shareitems").setExecutor(new CommandShareItems());
        this.getCommand("tpskeep").setExecutor(new CommandTPSKeep());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventBroadcastListener(), this);
        pluginManager.registerEvents(new AFK.EventListener(), this);
        pluginManager.registerEvents(new TPSKeeper.EventListener(), this);
        pluginManager.registerEvents(new ItemsViewer.EventListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, AFK::task, 200, 1200);
        if (isSparkExist) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, TPSKeeper::task, 1200, 1200);
        }
    }

    @Override
    public void onDisable() {
        Log.info("Plugin stop.");
    }
}
