package cc.akashic.insight;

import cc.akashic.insight.command.*;
import cc.akashic.insight.utils.ItemsViewer;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Objects;

public final class Insight extends JavaPlugin {
    public static Insight instance;
    public static File dataFolder;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;

        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        dataFolder = this.getDataFolder();

        this.saveDefaultConfig();
        config = this.getConfig();

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

        CustomRecipe.addRecipe();

        Objects.requireNonNull(this.getCommand("insight")).setExecutor(new CommandInsight());
        Objects.requireNonNull(this.getCommand("xray")).setExecutor(new CommandXray());
        Objects.requireNonNull(this.getCommand("shareitems")).setExecutor(new CommandShareItems());
        Objects.requireNonNull(this.getCommand("tpskeep")).setExecutor(new CommandTPSKeep());
        Objects.requireNonNull(this.getCommand("slogan")).setExecutor(new CommandSlogan());
        Objects.requireNonNull(this.getCommand("leader")).setExecutor(new CommandLeader());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventBroadcastListener(), this);
        pluginManager.registerEvents(new AFK.EventListener(), this);
        pluginManager.registerEvents(new TPSKeeper.EventListener(), this);
        pluginManager.registerEvents(new Slogan.EventListener(), this);
        pluginManager.registerEvents(new CustomRecipe.EventListener(), this);
        pluginManager.registerEvents(new JoinPrivateMessenger.EventListener(), this);
        pluginManager.registerEvents(new LeaderBoard.EventListener(), this);
        pluginManager.registerEvents(new ItemsViewer.EventListener(), this);

        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        bukkitScheduler.scheduleSyncRepeatingTask(this, AFK::task, 200L, 1200L);
        if (isSparkExist) {
            bukkitScheduler.scheduleSyncRepeatingTask(this, TPSKeeper::task, 1200L, 1200L);
        }

        int pluginId = 13612;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        Slogan.saveSlogan();

        Log.info("Plugin stop.");
    }
}
