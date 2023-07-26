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

        Objects.requireNonNull(this.getCommand("disableend")).setExecutor(new CommandDisableEnd());
        Objects.requireNonNull(this.getCommand("enableend")).setExecutor(new CommandEnableEnd());
        Objects.requireNonNull(this.getCommand("players")).setExecutor(new CommandPlayers());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventBroadcastListener(), this);
        pluginManager.registerEvents(new AFK.EventListener(), this);
        pluginManager.registerEvents(new JoinPrivateMessenger.EventListener(), this);
        pluginManager.registerEvents(new ItemsViewer.EventListener(), this);
        pluginManager.registerEvents(new DisableEnd.EventListener(), this);

        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        bukkitScheduler.scheduleSyncRepeatingTask(this, AFK::task, 200L, 1200L);

        int pluginId = 13612;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        Log.info("Plugin stop.");
    }
}
