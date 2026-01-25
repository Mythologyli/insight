package cc.akashic.insight;

import cc.akashic.insight.utils.ListNameEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.purpurmc.purpur.event.PlayerAFKEvent;

import java.util.HashSet;

public final class AFK {
    static final HashSet<Player> AFKPlayerSet = new HashSet<>();

    public static boolean isPlayerAFK(Player player) {
        return AFKPlayerSet.contains(player);
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            if (AFKPlayerSet.remove(player)) {
                ListNameEditor.setPlayerListNameAFKPrefix(player, "");
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerQuit(PlayerQuitEvent event) {
            AFKPlayerSet.remove(event.getPlayer());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerAFK(PlayerAFKEvent event) {
            Player player = event.getPlayer();

            if (event.isGoingAfk()) {
                if (AFKPlayerSet.add(player)) {
                    ListNameEditor.setPlayerListNameAFKPrefix(player, "[AFK]");
                }
            } else {
                if (AFKPlayerSet.remove(player)) {
                    ListNameEditor.setPlayerListNameAFKPrefix(player, "");
                }
            }
        }
    }
}
