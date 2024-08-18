package cc.akashic.insight.utils;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public final class Vanished {
    /**
     * Check if player is vanished.
     *
     * @param player Player
     * @return boolean
     */
    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }
}
