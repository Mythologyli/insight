package cc.akashic.insight;

import cc.akashic.insight.utils.Vanished;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.net.InetSocketAddress;
import java.util.Objects;

public final class EventBroadcastListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String addressString = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        InetSocketAddress haProxyAddress = player.getHAProxyAddress();
        String haProxyAddressString;
        if (haProxyAddress != null) {
            haProxyAddressString = haProxyAddress.getAddress().getHostAddress();
        } else {
            haProxyAddressString = "null";
        }

        Log.info("EVENT|JOIN|" + player.getName() + "|" + addressString + "|" + haProxyAddressString);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (Vanished.isVanished(player)) {
            return;
        }
        var deathMessage = event.deathMessage();

        StringBuilder componentString = new StringBuilder();

        if (deathMessage instanceof TranslatableComponent translatableDeathMessage) {
            componentString.append("K,").append(translatableDeathMessage.key());
            for (var argument : translatableDeathMessage.arguments()) {
                var value = argument.value();
                if (value instanceof TranslatableComponent translatableArgument) {
                    componentString.append(";K,").append(translatableArgument.key());
                } else if (value instanceof Component component) {
                    componentString.append(";T,").append(PlainTextComponentSerializer.plainText().serialize(component));
                } else {
                    componentString.append(";T,").append(value);
                }
            }
        } else {
            assert deathMessage != null;
            componentString.append("T,").append(PlainTextComponentSerializer.plainText().serialize(deathMessage));
        }

        Location location = player.getLocation();
        var locationString = location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName();

        Log.info("EVENT|DEAD|" + player.getName() + "|" + componentString + "|" + player.getLevel() + "|" + locationString);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (Vanished.isVanished(player)) {
            return;
        }
        var advancementDisplay = event.getAdvancement().displayName();
        String advancementString = "";

        if (advancementDisplay instanceof TranslatableComponent translatableAdvancementDisplay) {
            for (var argument : translatableAdvancementDisplay.arguments()) {
                if (argument.value() instanceof TranslatableComponent translatableArgument) {
                    advancementString = translatableArgument.key();
                }
            }
        }

        if (advancementString.equals("")) {
            return;
        }

        Log.info("EVENT|ADVANCEMENT|" + player.getName() + "|" + advancementString);
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        Player player = event.getPlayer();
        if (Vanished.isVanished(player)) {
            return;
        }
        Location location = player.getLocation();

        Log.info("EVENT|RAIDTRIGGER|" + player.getName() + "|" + location.getX() + "," + location.getY() + "," + location.getZ());
    }
}
