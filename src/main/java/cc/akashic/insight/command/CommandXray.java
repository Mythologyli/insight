package cc.akashic.insight.command;

import cc.akashic.insight.utils.ShowItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class CommandXray implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage("Player not found!");
            return true;
        }

        ItemStack[] items;

        switch (args[1]) {
            case "inventory":
                items = player.getInventory().getContents();

                break;

            case "enderchest":
                items = player.getEnderChest().getContents();

                break;

            case "hand":
                items = new ItemStack[1];
                items[0] = player.getInventory().getItemInMainHand();

                break;

            default:
                sender.sendMessage("Error input!");
                return false;
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");
        sender.sendMessage(ChatColor.AQUA + "Player: " + playerName);

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.YELLOW + "Open a Xray result box.");
            ShowItems.guiToPlayer(items, (Player) sender,  "Xray of " + playerName + "'s Items");
        } else {
            ShowItems.printToConsole(items);
        }

        sender.sendMessage(ChatColor.GREEN + "=========================");

        return true;
    }
}
