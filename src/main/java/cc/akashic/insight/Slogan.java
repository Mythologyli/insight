package cc.akashic.insight;

import cc.akashic.insight.utils.ListNameEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public final class Slogan {
    private static YamlConfiguration config;

    public static void loadSlogan() {
        config = YamlConfiguration.loadConfiguration(new File(Insight.dataFolder + "/slogan.yml"));

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            ListNameEditor.setPlayerListNameSuffix(player, config.getString("Slogan." + player.getName(), ""));
        }
    }

    public static void saveSlogan() {
        try {
            config.save(new File(Insight.dataFolder + "/slogan.yml"));
        } catch (IOException e) {
            Log.info("Fail to save slogan.yml!");
        }
    }

    public static void setPlayerSlogan(Player player, String slogan) {
        slogan = ChatColor.LIGHT_PURPLE + "[" + slogan + "]";
        ListNameEditor.setPlayerListNameSuffix(player, slogan);
        config.set("Slogan." + player.getName(), slogan);
    }

    public static void clearPlayerSlogan(Player player) {
        ListNameEditor.setPlayerListNameSuffix(player, "");
        config.set("Slogan." + player.getName(), "");
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            ListNameEditor.setPlayerListNameSuffix(player, config.getString("Slogan." + player.getName(), ""));
        }
    }
}
