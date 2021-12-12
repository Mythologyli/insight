package cc.akashic.insight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

public final class EventListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Log.info("EVENT|DEAD|" + player.getName() + "|" + event.getDeathMessage() + "|" + player.getLevel());
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        Player player = event.getPlayer();

        Log.info("EVENT|RAID|" + player.getName());
    }
}
