package cc.akashic.insight.command;

import cc.akashic.insight.TPSKeeper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandTPSKeep implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("true")) {
                TPSKeeper.setIsInForceTPSKeepMode(true);
                sender.sendMessage("Set isInForceTPSKeepMode true.");
            } else if (args[0].equals("false")) {
                TPSKeeper.setIsInForceTPSKeepMode(false);
                sender.sendMessage("Set isInForceTPSKeepMode false.");
            } else {
                return false;
            }
        } else if (args.length == 2) {
            if (args[0].equals("set")) {
                try {
                    TPSKeeper.setTPSTriggerValue(Double.parseDouble(args[1]));
                    sender.sendMessage("Set TPSTriggerValue " + Double.parseDouble(args[1]) + " .");
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
