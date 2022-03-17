package cc.akashic.insight.command;

import cc.akashic.insight.SpectatorKeeper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CommandKeepSpectator implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player playerSender)) {
            sender.sendMessage("This command can only run in game!");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        if (args[0].equals("on")) {
            if (playerSender.getSpectatorTarget() instanceof Player) {
                SpectatorKeeper.setPlayerSpectatePlayer(playerSender, (Player) playerSender.getSpectatorTarget());
            } else {
                sender.sendMessage("You are not in spectator mode or you are not following any player!");
            }
        } else if (args[0].equals("off")) {
            SpectatorKeeper.removePlayerSpectatePlayer(playerSender);
        } else {
            return false;
        }

        return true;
    }
}
