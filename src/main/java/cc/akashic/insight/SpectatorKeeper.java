package cc.akashic.insight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class SpectatorKeeper {
    static private final HashMap<Player, Player> playerSpectatePlayer = new HashMap<>();
    static private final HashMap<Player, Boolean> spectatorLock = new HashMap<>();
    static private int taskID;

    public static void setPlayerSpectatePlayer(Player playerSpectator, Player playerSpectated) {
        if (playerSpectatePlayer.isEmpty()) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Insight.instance, SpectatorKeeper::task, 200L, 20L);
        }

        playerSpectatePlayer.put(playerSpectator, playerSpectated);
        spectatorLock.put(playerSpectator, false);
    }

    public static void removePlayerSpectatePlayer(Player playerSpectator) {
        playerSpectatePlayer.remove(playerSpectator);
        spectatorLock.remove(playerSpectator);

        if (playerSpectatePlayer.isEmpty()) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    public static void task() {
        for (HashMap.Entry<Player, Player> entry : playerSpectatePlayer.entrySet()) {
            Player playerSpectator = entry.getKey();
            Player playerSpectated = entry.getValue();

            if (spectatorLock.get(playerSpectator)) {
                continue;
            }

            if (playerSpectator.getGameMode() != GameMode.SPECTATOR || !playerSpectator.isOnline() || !playerSpectated.isOnline()) {
                playerSpectatePlayer.remove(playerSpectator);
                spectatorLock.remove(playerSpectator);
                continue;
            }

            if (playerSpectator.getSpectatorTarget() != playerSpectated || playerSpectator.getWorld() != playerSpectated.getWorld() || playerSpectator.getLocation().distance(playerSpectated.getLocation()) > 2.0) {
                spectatorLock.put(playerSpectator, true);
                Bukkit.getServer().dispatchCommand(playerSpectator, "spectate");

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Insight.instance, () -> {
                    Bukkit.getServer().dispatchCommand(playerSpectator, "spectate " + playerSpectated.getName());
                    spectatorLock.put(playerSpectator, false);
                }, 100L);
            }
        }
    }
}
