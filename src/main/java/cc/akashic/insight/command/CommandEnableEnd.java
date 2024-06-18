package cc.akashic.insight.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cc.akashic.insight.DisableEnd.enableEnd;

public class CommandEnableEnd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        enableEnd();

        sender.sendMessage("World end has been enabled!");

        return true;
    }
}
