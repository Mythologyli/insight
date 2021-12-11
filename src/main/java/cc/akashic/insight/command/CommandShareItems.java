package cc.akashic.insight.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;

public class CommandShareItems extends CommandXray {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inventory = player.getInventory();

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");
            Bukkit.broadcastMessage("Player " + player.getName() + " shared his inventory!");

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player playerToSend : players) {
                displayInventory(inventory, playerToSend);
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + "=========================");

        } else {
            sender.sendMessage("This command can only run in game!");
        }

        return true;
    }
}
