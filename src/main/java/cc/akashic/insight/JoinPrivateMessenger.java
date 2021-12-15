package cc.akashic.insight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinPrivateMessenger {
    public static final class EventListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            event.getPlayer().sendMessage(Insight.config.getString("JoinPrivateMessenger.message", ""));
        }
    }
}
