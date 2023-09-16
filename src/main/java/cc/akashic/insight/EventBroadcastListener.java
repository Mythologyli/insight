package cc.akashic.insight;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.List;

public final class EventBroadcastListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Component deathMessage = event.deathMessage();

        StringBuilder componentString = new StringBuilder();

        if (deathMessage instanceof TranslatableComponent) {
            List<Component> args = ((TranslatableComponent) deathMessage).args();

            componentString.append("K,").append(((TranslatableComponent) deathMessage).key());
            for (Component arg : args) {
                if (arg instanceof TranslatableComponent) {
                    componentString.append(";K,").append(((TranslatableComponent) arg).key());
                } else {
                    componentString.append(";T,").append(PlainComponentSerializer.plain().serialize(arg));
                }
            }
        } else {
            assert deathMessage != null;
            componentString.append("T,").append(PlainComponentSerializer.plain().serialize(deathMessage));
        }

        Location location = player.getLocation();
        var locationString = location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName();

        Log.info("EVENT|DEAD|" + player.getName() + "|" + componentString + "|" + player.getLevel() + "|" + locationString);
    }

//    @EventHandler
//    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
//        Player player = event.getPlayer();
//        var advancementDisplay = event.getAdvancement().displayName();
//        String advancementString = "";
//
//        if (advancementDisplay instanceof TranslatableComponent) {
//            var args = ((TranslatableComponent) advancementDisplay).args();
//
//            for (var arg : args) {
//                if (arg instanceof TranslatableComponent) {
//                    advancementString = ((TranslatableComponent) arg).key();
//                }
//            }
//        }
//
//        if (advancementString.equals("")) {
//            return;
//        }
//
//        Log.info("EVENT|ADVANCEMENT|" + player.getName() + "|" + advancementString);
//    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Log.info("EVENT|RAIDTRIGGER|" + player.getName() + "|" + location.getX() + "," + location.getY() + "," + location.getZ());
    }
}
