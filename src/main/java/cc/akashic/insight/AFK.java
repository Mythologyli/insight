package cc.akashic.insight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.LinkedList;

public final class AFK {
    static LinkedList<String> AFKPlayerNameList = new LinkedList<>();
    static LinkedList<String> activePlayerNameList = new LinkedList<>();

    /**
     * Set a player's state to active.
     * Remove him from AFK if he was in.
     *
     * @param player player
     */
    private static void setPlayerStateToActive(Player player) {
        String playerName = player.getName();

        if (!activePlayerNameList.contains(playerName)) {
            activePlayerNameList.add(playerName);

            if (AFKPlayerNameList.contains(playerName)) {
                AFKPlayerNameList.remove(playerName);
                player.setPlayerListName(playerName);
                player.setSleepingIgnored(false);
            }
        }
    }

    /**
     * Set a player's state to active. Send a message to everyone.
     * Remove him from AFK if he was in.
     *
     * @param player player
     */
    private static void setPlayerStateToActive(Player player, String msg) {
        String playerName = player.getName();

        if (!activePlayerNameList.contains(playerName)) {
            activePlayerNameList.add(playerName);

            if (AFKPlayerNameList.contains(playerName)) {
                AFKPlayerNameList.remove(playerName);
                player.setPlayerListName(playerName);
                player.setSleepingIgnored(false);

                Bukkit.broadcastMessage(msg);
            }
        }
    }

    /**
     * Repeatedly run this task. The period is the AFK time.
     */
    public static void task() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            String playerName = player.getName();

            if (!activePlayerNameList.contains(playerName) && !AFKPlayerNameList.contains(playerName)) {
                AFKPlayerNameList.add(playerName);
                player.setPlayerListName(playerName + ChatColor.YELLOW + "[AFK]");
                player.setSleepingIgnored(true);

                Bukkit.broadcastMessage(ChatColor.YELLOW + playerName + " is away from keyboard!");
            }
        }

        for (String playerName : AFKPlayerNameList) {
            if (Bukkit.getPlayer(playerName) == null) {
                AFKPlayerNameList.remove(playerName);

                Log.info("Remove offline player " + playerName + " from AFK.");
            }
        }

        activePlayerNameList.clear();
    }

    public static final class EventListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            event.getPlayer().setSleepingIgnored(false);
            setPlayerStateToActive(event.getPlayer());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerQuit(PlayerQuitEvent event) {
            setPlayerStateToActive(event.getPlayer());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerMove(PlayerMoveEvent event) {
            setPlayerStateToActive(event.getPlayer(), ChatColor.YELLOW + event.getPlayer().getName() + " is back now!");
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerInteract(PlayerInteractEvent event) {
            setPlayerStateToActive(event.getPlayer(), ChatColor.YELLOW + event.getPlayer().getName() + " is back now!");
        }
    }
}
