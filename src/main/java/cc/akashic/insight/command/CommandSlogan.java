package cc.akashic.insight.command;

import cc.akashic.insight.Slogan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class CommandSlogan implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0]) {
            case "set" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This command can only run in game!");
                    return true;
                }

                StringBuilder slogan = new StringBuilder();

                int i;
                for (i = 1; i < args.length - 1; i++) {
                    slogan.append(args[i]).append(" ");
                }
                slogan.append(args[i]);

                // Check if slogan is too long
                if (slogan.length() > 16) {
                    sender.sendMessage("Slogan is longer than 16 characters!");
                    return true;
                }

                Slogan.setPlayerSlogan((Player) sender, slogan.toString());
            }

            case "clear" -> {
                if (args.length == 1) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("This command can only run in game!");
                        return true;
                    }

                    Slogan.clearPlayerSlogan((Player) sender);
                } else if (args.length == 2) {
                    if (sender.hasPermission("Insight.slogan.clearothers")) {
                        Player playerToClear = Bukkit.getPlayer(args[1]);

                        if (playerToClear != null) {
                            Slogan.clearPlayerSlogan(playerToClear);
                            sender.sendMessage("Slogan clear!");
                        } else {
                            sender.sendMessage("Player not found!");
                        }
                    } else {
                        sender.sendMessage("You don't have permission to do this!");
                    }
                } else {
                    return false;
                }
            }

            case "reload" -> {
                if (sender.hasPermission("Insight.slogan.reload")) {
                    if (args.length != 1) {
                        return false;
                    }

                    Slogan.loadSlogan();
                    sender.sendMessage("Slogan reload!");
                } else {
                    sender.sendMessage("You don't have permission to do this!");
                }
            }

            case "save" -> {
                if (sender.hasPermission("Insight.slogan.save")) {
                    if (args.length != 1) {
                        return false;
                    }

                    Slogan.saveSlogan();
                    sender.sendMessage("Slogan saved!");
                } else {
                    sender.sendMessage("You don't have permission to do this!");
                }
            }

            default -> {
                return false;
            }
        }

        return true;
    }
}
