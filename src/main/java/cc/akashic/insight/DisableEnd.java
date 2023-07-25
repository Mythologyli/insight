package cc.akashic.insight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public final class DisableEnd {
    private static boolean isEndDisabled = false;


    public static void disableEnd() {
        isEndDisabled = true;
    }

    public static void enableEnd() {
        isEndDisabled = false;
    }

    public static final class EventListener implements Listener {
        @EventHandler
        public void onPlayerPortal(PlayerPortalEvent event) {
            if (isEndDisabled) {
                if (event.getCause() == PlayerPortalEvent.TeleportCause.END_PORTAL) {
                    event.setCancelled(true);

                    // Send message to player
                    event.getPlayer().sendMessage("World end has been disabled!");
                }
            }
        }
    }
}
