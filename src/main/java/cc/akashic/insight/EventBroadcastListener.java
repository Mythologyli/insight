package cc.akashic.insight;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.Objects;

public final class EventBroadcastListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Log.info("EVENT|DEAD|" + player.getName() + "|" + PaperComponents.plainTextSerializer().serialize(Objects.requireNonNull(event.deathMessage())) + "|" + player.getLevel());
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Log.info("EVENT|RAIDTRIGGER|" + player.getName() + "|" + location.getX() + "," + location.getY() + "," + location.getZ());
    }
}
