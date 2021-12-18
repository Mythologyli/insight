package cc.akashic.insight.command;

import cc.akashic.insight.LeaderBoard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandLeader implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            LeaderBoard.openToPlayer((Player) sender);
        }

        return true;
    }
}
