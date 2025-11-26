package cc.akashic.insight.command;

import cc.akashic.insight.AFK;
import cc.akashic.insight.utils.Vanished;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Objects;

public class CommandPlayers implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        StringBuilder text = new StringBuilder("PLAYERS|");

        for (Player player : Bukkit.getOnlinePlayers()) {
            InetSocketAddress haProxyAddress = player.getHAProxyAddress();
            String haProxyAddressString;
            if (haProxyAddress != null) {
                haProxyAddressString = haProxyAddress.getAddress().getHostAddress();
            } else {
                haProxyAddressString = "null";
            }

            text.append(player.getName())
                    .append(",")
                    .append((float) (Math.ceil(player.getHealth()) / 2.0))
                    .append(",")
                    .append(player.getPing())
                    .append(",")
                    .append(AFK.isPlayerAFK(player))
                    .append(",")
                    .append(Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress())
                    .append(",")
                    .append(haProxyAddressString)
                    .append(",")
                    .append(Vanished.isVanished(player))
                    .append(";");
        }

        sender.sendMessage(text.toString());

        return true;
    }
}
