package cc.akashic.insight.command;

import cc.akashic.insight.utils.ItemsViewer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
        int inventorySize;

        switch (args[1]) {
            case "inventory" -> {
                items = player.getInventory().getContents();
                inventorySize = 45;
            }
            case "enderchest" -> {
                items = player.getEnderChest().getContents();
                inventorySize = 27;
            }
            case "hand" -> {
                items = new ItemStack[1];
                items[0] = player.getInventory().getItemInMainHand();
                inventorySize = 9;
            }
            default -> {
                sender.sendMessage("Error input!");
                return false;
            }
        }

        sender.sendMessage(Component.text("-------------------------", NamedTextColor.GREEN));
        sender.sendMessage(Component.text("Player: " + playerName, NamedTextColor.AQUA));

        if (sender instanceof Player) {
            sender.sendMessage(Component.text("Open a Xray result box.", NamedTextColor.YELLOW));
            ItemsViewer.guiToPlayer(items, (Player) sender, inventorySize, "Xray of " + playerName + "'s Items", 1200L);
        } else {
            ItemsViewer.printToConsole(items);
        }

        sender.sendMessage(Component.text("-------------------------", NamedTextColor.GREEN));

        return true;
    }
}
