package cc.akashic.insight;

import cc.akashic.insight.utils.ListNameEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashSet;

public final class AFK {
    static final HashSet<Player> AFKPlayerSet = new HashSet<>();
    static final HashSet<Player> activePlayerSet = new HashSet<>();
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();

    /**
     * Set a player's state to active.
     * Remove him from AFK if he was in.
     *
     * @param player player
     */
    private static void setPlayerStateToActive(Player player) {
        if (!activePlayerSet.contains(player)) {
            activePlayerSet.add(player);

            if (AFKPlayerSet.contains(player)) {
                AFKPlayerSet.remove(player);
                ListNameEditor.setPlayerListNamePrefix(player, "");
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
    private static void setPlayerStateToActive(Player player, TextComponent msg) {
        if (!activePlayerSet.contains(player)) {
            activePlayerSet.add(player);

            if (AFKPlayerSet.contains(player)) {
                AFKPlayerSet.remove(player);
                ListNameEditor.setPlayerListNamePrefix(player, "");
                player.setSleepingIgnored(false);

                Bukkit.broadcast(legacyComponentSerializer.serialize(msg), "");
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

            if (!activePlayerSet.contains(player) && !AFKPlayerSet.contains(player)) {
                AFKPlayerSet.add(player);
                ListNameEditor.setPlayerListNamePrefix(player, "[AFK]");
                player.setSleepingIgnored(true);

                Bukkit.broadcast(legacyComponentSerializer.serialize(Component.text(playerName + " is away from keyboard!", NamedTextColor.YELLOW)), "");
            }
        }

        for (Player player : AFKPlayerSet) {
            if (!player.isOnline()) {
                AFKPlayerSet.remove(player);

                Log.info("Remove offline player " + player.getName() + " from AFK.");
            }
        }

        activePlayerSet.clear();
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
            setPlayerStateToActive(event.getPlayer(), Component.text(event.getPlayer().getName() + " is back now!", NamedTextColor.YELLOW));
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerInteract(PlayerInteractEvent event) {
            setPlayerStateToActive(event.getPlayer(), Component.text(event.getPlayer().getName() + " is back now!", NamedTextColor.YELLOW));
        }
    }
}
