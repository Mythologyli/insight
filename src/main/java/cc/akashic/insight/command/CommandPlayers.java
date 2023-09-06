package cc.akashic.insight.command;

import cc.akashic.insight.AFK;
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
            text.append(player.getName())
                    .append(",")
                    .append((float) (Math.ceil(player.getHealth()) / 2.0))
                    .append(",")
                    .append(player.getPing())
                    .append(",")
                    .append(AFK.isPlayerAFK(player))
                    .append(";");
        }

        sender.sendMessage(text.toString());

        return true;
    }
}
