package cc.akashic.insight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class SpectatorKeeper {
    static private final HashMap<Player, Player> playerSpectatePlayer = new HashMap<>();
    static private int taskID;

    public static void setPlayerSpectatePlayer(Player playerSpectator, Player playerSpectated) {
        if (playerSpectatePlayer.isEmpty()) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Insight.instance, SpectatorKeeper::task, 200L, 20L);
        }

        playerSpectatePlayer.put(playerSpectator, playerSpectated);
    }

    public static void removePlayerSpectatePlayer(Player playerSpectator) {
        playerSpectatePlayer.remove(playerSpectator);

        if (playerSpectatePlayer.isEmpty()) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    public static void task() {
        for (HashMap.Entry<Player, Player> entry : playerSpectatePlayer.entrySet()) {
            Player playerSpectator = entry.getKey();
            Player playerSpectated = entry.getValue();

            if (playerSpectator.getGameMode() != GameMode.SPECTATOR || !playerSpectator.isOnline() || !playerSpectated.isOnline()) {
                playerSpectatePlayer.remove(playerSpectator);
                continue;
            }

            if (playerSpectator.getWorld() != playerSpectated.getWorld()) {
                playerSpectator.setSpectatorTarget(null);
                playerSpectator.teleport(playerSpectated.getLocation());

                Bukkit.getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> playerSpectator.setSpectatorTarget(playerSpectated), 20L);

                continue;
            }

            if (playerSpectator.getSpectatorTarget() != playerSpectated) {
                playerSpectator.setSpectatorTarget(playerSpectated);
            }
        }
    }
}
