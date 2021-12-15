package cc.akashic.insight;

import cc.akashic.insight.command.*;
import cc.akashic.insight.utils.ItemsViewer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Insight extends JavaPlugin {
    public static Insight instance;
    public static File dataFolder;

    @Override
    public void onEnable() {
        instance = this;

        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        dataFolder = this.getDataFolder();

        File sloganFile = new File(dataFolder + "/slogan.yml");
        if (!sloganFile.exists()) {
            saveResource("slogan.yml", false);
        }

        File tpsFile = new File(dataFolder + "/tps.txt");
        if (!tpsFile.exists()) {
            saveResource("tps.txt", false);
        }

        boolean isSparkExist = TPSKeeper.getSpark();
        TPSKeeper.getWorld();
        TPSKeeper.saveOriginMonsterSpawnLimit();
        TPSKeeper.disableTPSKeepMode();

        Slogan.loadSlogan();

        this.getCommand("insight").setExecutor(new CommandInsight());
        this.getCommand("xray").setExecutor(new CommandXray());
        this.getCommand("shareitems").setExecutor(new CommandShareItems());
        this.getCommand("tpskeep").setExecutor(new CommandTPSKeep());
        this.getCommand("slogan").setExecutor(new CommandSlogan());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventBroadcastListener(), this);
        pluginManager.registerEvents(new AFK.EventListener(), this);
        pluginManager.registerEvents(new TPSKeeper.EventListener(), this);
        pluginManager.registerEvents(new Slogan.EventListener(), this);
        pluginManager.registerEvents(new ItemsViewer.EventListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, AFK::task, 200, 1200);
        if (isSparkExist) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, TPSKeeper::task, 1200, 1200);
        }
    }

    @Override
    public void onDisable() {
        Slogan.saveSlogan();

        Log.info("Plugin stop.");
    }
}
