package cc.akashic.insight.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandPlayers implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        StringBuilder text = new StringBuilder("PLAYERS|");

        for (Player player : Bukkit.getOnlinePlayers()) {
            int ping = player.getPing();

            float hearts = (float) (Math.ceil(player.getHealth()) / 2.0);

            text.append(player.getName()).append(",").append(hearts).append(",").append(ping).append(";");
        }

        sender.sendMessage(text.toString());

        return true;
    }
}
