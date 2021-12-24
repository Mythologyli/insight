package cc.akashic.insight;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class SpectatorKeeper {
    static HashMap<Player, Player> playerSpectatePlayer = new HashMap<>();

    public static void setPlayerSpectatePlayer(Player playerSpectator, Player playerSpectated) {
        playerSpectatePlayer.put(playerSpectator, playerSpectated);
    }

    public static void removePlayerSpectatePlayer(Player playerSpectator) {
        playerSpectatePlayer.remove(playerSpectator);
    }

    public static void task() {
        for (HashMap.Entry<Player, Player> entry : playerSpectatePlayer.entrySet()) {
            Player playerSpectator = entry.getKey();
            Player playerSpectated = entry.getValue();

            if (playerSpectator.getGameMode() != GameMode.SPECTATOR || !playerSpectator.isOnline() || !playerSpectated.isOnline()) {
                playerSpectatePlayer.remove(playerSpectator);
                continue;
            }

            if (playerSpectator.getSpectatorTarget() != playerSpectated) {
                playerSpectator.setSpectatorTarget(playerSpectated);
            }
        }
    }
}
