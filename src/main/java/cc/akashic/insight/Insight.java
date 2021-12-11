package cc.akashic.insight;

import cc.akashic.insight.command.CommandInsight;
import org.bukkit.plugin.java.JavaPlugin;

public final class Insight extends JavaPlugin {
    @Override
    public void onEnable() {
        Log.setLogger(getLogger());
        Log.info("Plugin start.");

        this.getCommand("insight").setExecutor(new CommandInsight());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        Log.info("Plugin stop.");
    }
}
