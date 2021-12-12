package cc.akashic.insight.command;

import cc.akashic.insight.utils.ShowItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public final class CommandShareItems implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack[] items;

            if (args.length == 0) {
                items = player.getInventory().getContents();

                Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");
                Bukkit.broadcastMessage(ChatColor.AQUA + "Player " + player.getName() + " shared his inventory!");
            } else {
                if (args.length > 1) {
                    return false;
                }

                switch (args[0]) {
                    case "hand":
                        items = new ItemStack[1];
                        items[0] = player.getInventory().getItemInMainHand();

                        Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Player " + player.getName() + " shared his items in hand!");

                        break;

                    default:
                        sender.sendMessage("Error input!");
                        return false;
                }
            }

            ShowItems.printToConsole(items);

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player playerToSend : players) {
                ShowItems.printToPlayer(items, playerToSend);
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");

        } else {
            sender.sendMessage("This command can only run in game!");
        }

        return true;
    }
}
