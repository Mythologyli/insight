package cc.akashic.insight.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class CommandShareItems extends CommandXray {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack[] items = player.getInventory().getContents();

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");
            Bukkit.broadcastMessage(ChatColor.AQUA + "Player " + player.getName() + " shared his inventory!");

            printItems(items);

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player playerToSend : players) {
                printItems(items, playerToSend);
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");

        } else {
            sender.sendMessage("This command can only run in game!");
        }

        return true;
    }
}
